/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipClassLoader extends ClassLoader {
    // This code was posted on a forum by "leukbr" on March 30, 2001.
    // http://forums.sun.com/thread.jspa?threadID=360060&forumID=31
    // I've modified it substantially to include a thread that keeps the file
    // open for OPEN_TIME milliseconds so time isn't wasted continually
    // opening and closing the file.

    private static final int OPEN_TIME = 5000;
    private static final int REQUEST_FIND = 0;
    private static final int REQUEST_LOAD = 1;

    private static class Request {
        private final int action;
        private final String resource;

        private volatile boolean responseSent;
        private Object response;

        Request(int action, String resource) {
            this.action = action;
            this.resource = resource;
            this.responseSent = false;
        }

        @Override
        public String toString() {
            String act = action == REQUEST_LOAD ? "load"
                    : action == REQUEST_FIND ? "find" : "act" + action;
            return act + ":" + resource;
        }

        void setResponse(Object value) {
            synchronized (this) {
                response = value;
                responseSent = true;
                notifyAll();
            }
        }

        void ensureDone() {
            synchronized (this) {
                if (!responseSent) {
                    responseSent = true;
                    response = null;
                    notifyAll();
                }
            }

        }

        Object getResponse() {
            synchronized (this) {
                while (!responseSent) {
                    try {
                        this.wait(1000);
                    } catch (InterruptedException ignored) {
                    }
                }
                return response;
            }
        }
    }

    private class WorkThread extends Thread {
        private final LinkedList<Request> requests = new LinkedList<>();
        private ZipFile zipFile = null;

        @Override
        public void run() {
            try {
                while (true) {
                    Request request = waitForNextRequest();
                    if (request == null) {
                        return;
                    }

                    try {
                        switch (request.action) {
                            case REQUEST_LOAD:
                                performLoad(request);
                                break;
                            case REQUEST_FIND:
                                performFind(request);
                                break;
                        }
                    } finally {
                        request.ensureDone();
                    }
                }
            } catch (Exception t) {
                t.printStackTrace();

            } finally {
                if (zipFile != null) {
                    try {
                        zipFile.close();
                        zipFile = null;
                    } catch (IOException ignored) {
                    }
                }
            }
        }

        private Request waitForNextRequest() {
            synchronized (bgLock) {
                long start = System.currentTimeMillis();
                while (requests.isEmpty()) {
                    long elapse = System.currentTimeMillis() - start;
                    if (elapse >= OPEN_TIME) {
                        bgThread = null;
                        return null;
                    }
                    try {
                        bgLock.wait(OPEN_TIME);
                    } catch (InterruptedException ignored) {
                    }
                }
                return requests.removeFirst();
            }
        }

        private void performFind(Request request) {
            ensureZipOpen();
            Object responseValue = null;
            if (zipFile != null) {
                String resource = request.resource;
                ZipEntry zipEntry = zipFile.getEntry(resource);
                if (zipEntry != null) {
                    String url = "jar:" + zipPath.toURI() + "!/" + resource;
                    try {
                        responseValue = new URL(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
            request.setResponse(responseValue);
        }

        private void performLoad(Request request) {
            BufferedInputStream bis = null;
            ensureZipOpen();
            Object responseValue = null;
            try {
                if (zipFile != null) {

                    ZipEntry zipEntry = zipFile.getEntry(request.resource);
                    if (zipEntry != null) {

                        byte[] result = new byte[(int) zipEntry.getSize()];
                        bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                        try {
                            bis.read(result, 0, result.length);
                            responseValue = result;
                        } catch (IOException ignored) {
                        }
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();

            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException ignored) {
                    }
                }
            }
            request.setResponse(responseValue);
        }

        private void ensureZipOpen() {
            if (zipFile == null) {
                try {
                    zipFile = new ZipFile(zipPath);
                } catch (IOException ignored) {
                }
            }
        }
    }

    private final File zipPath;
    private final Map<String, Object> classes = new HashMap<>();
    private final Object bgLock = new Object();

    private WorkThread bgThread = null;

    /**
     * Constructs a zip class loader for the given zip file name.
     *
     * @param zipFileName the zip file name
     */
    public ZipClassLoader(String zipFileName) {
        this(new File(zipFileName));
    }

    /**
     * Constructs a zip class loader for the given zip file.
     *
     * @param zipFile the zip file
     */
    public ZipClassLoader(File zipFile) {
        zipPath = zipFile;
    }

    @Override
    public URL findResource(String resourceName) {
        Object ret = request(REQUEST_FIND, resourceName);
        if (ret instanceof URL) {
            return (URL) ret;
        }
        return super.findResource(resourceName);
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        boolean found;
        Object result = null;

        // check whether we have loaded this class before
        synchronized (classes) {
            found = classes.containsKey(className);
            if (found) {
                result = classes.get(className);
            }
        }

        // try loading it from the ZIP file if we haven't
        if (!found) {
            String resourceName = className.replace('.', '/') + ".class";
            result = request(REQUEST_LOAD, resourceName);

            if (result instanceof byte[]) {
                byte[] data = (byte[]) result;
                result = defineClass(className, data, 0, data.length);
                if (result == null) {
                    result = new ClassFormatError(className);
                }
            }

            synchronized (classes) {
                classes.put(className, result);
            }
        }

        if (result instanceof Class) {
            return (Class<?>) result;
        }
        if (result instanceof ClassNotFoundException) {
            throw (ClassNotFoundException) result;
        }
        if (result instanceof Error) {
            throw (Error) result;
        }
        return super.findClass(className);
    }

    private Object request(int action, String resourceName) {
        Request request;

        synchronized (bgLock) {
            // start the thread if it isn't working
            if (bgThread == null) {
                bgThread = new WorkThread();
                bgThread.start();
            }
            request = new Request(action, resourceName);
            bgThread.requests.addLast(request);
            bgLock.notifyAll();
        }
        return request.getResponse();
    }
}
