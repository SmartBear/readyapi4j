package io.swagger.assert4j.util.rest.local;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import io.swagger.assert4j.util.rest.JsonTestObject;
import io.swagger.assert4j.util.rest.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import static io.swagger.assert4j.util.PortFinder.portFinder;
import static spark.Spark.*;

public class LocalServerUtil {

    public static class MyAnswer {

        public MyAnswer(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private String message;

    }

    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_PATH = "/";
    private static final String DEFAULT_MESSAGE = "Hello world";
    private static final String FORMAT = "application/json";

    private static JsonTestObject postedObject;

    private static Logger logger = LoggerFactory.getLogger(LocalServerUtil.class);

    /**
     * Starts a webserver for local testing. Uses first available port, starting for default port: 8080
     * <p>
     * Maps the default path: "/" with the default message: "Hello World", meaning that a call to
     * http://localhost:8080/ would result in Json: {message: "Hello World"}
     *
     * @return The actual port chosen for the server
     */
    public static int startLocalServer() {
        return startLocalServer(DEFAULT_PORT);
    }

    /**
     * Start a webserver for local testing. Uses first available port, starting from the given port
     * <p>
     * Maps the default path: "/" with the default message: "Hello World", meaning that a call to
     * http://localhost:<code>firstPort</code>/ would result in Json: {message: "Hello World"}
     *
     * @param firstPort The starting point for the available port search
     * @return The actual port chosen for the server
     */
    public static int startLocalServer(int firstPort) {
        return startLocalServer(firstPort, new Pair<>(DEFAULT_PATH, DEFAULT_MESSAGE));
    }

    /**
     * Start a webserver for local testing. Uses first available port, starting from the given port
     * <p>
     * Maps the given paths with the given messages in the given pairs, meaning that a call to
     * http://localhost:<code>firstport + pathMessages.getLeft()</code> would result in Json:
     * {message: <code>pathMessages.getRight()</code>}
     *
     * @param firstPort    The starting point for the available port search
     * @param pathMessages The pairs of paths and messages that are given back in json form
     * @return The actual port chosen for the server
     */
    @SafeVarargs
    public static int startLocalServer(int firstPort, Pair<String, String>... pathMessages) {
        int port = portFinder(firstPort);
        port(port);
        for (Pair<String, String> pathMessage : pathMessages) {
            get(pathMessage.getLeft(), FORMAT, (request, response) -> new MyAnswer(pathMessage.getRight()),
                    new LocalServerJsonTransformer());
        }
        before((request, response) -> logger.info(requestInfoToString(request)));
        return port;
    }

    /**
     * Start a webserver for local testing. Uses first available port, starting from the given port
     * <p>
     * Maps the given paths with the json from the given pojo in the given pairs, meaning that a call to
     * http://localhost:<code>firstport + pathMessages.getLeft()</code> would result in Json structure
     * from the given pojo
     *
     * @param firstPort The starting point for the available port search
     * @param pathJsons The pairs of paths and pojos that are converted to json structure
     * @return The actual port chosen for the server
     */
    @SafeVarargs
    public static int startLocalServerWithJsonObjects(int firstPort, Pair<String, JsonTestObject>... pathJsons) {
        int port = portFinder(firstPort);
        port(port);
        for (Pair<String, JsonTestObject> pathJson : pathJsons) {
            get(pathJson.getLeft(), FORMAT, (request, response) -> pathJson.getRight(),
                    new LocalServerJsonTransformer());
        }
        before((request, response) -> logger.info(requestInfoToString(request)));
        return port;
    }

    /**
     * Adds a new get rest method on a new server location/path to the localserver with a Json response of:
     * {message: <code>message</code>}
     * <p>
     * NOTE: If server is not started before calling this function, then it will start on localhost and port: 4567
     *
     * @param path    The path of the new server location
     * @param message The message to be added to the response json
     */
    public static void addGetToLocalServer(String path, String message) {
        get(path, FORMAT, (request, response) -> new MyAnswer(message),
                new LocalServerJsonTransformer());
    }

    /**
     * Adds a new get rest method on a new server location/path to the localserver with a Json response with the JsonTestObject
     * that is sent in to this method.
     * <p>
     * NOTE: If server is not started before calling this function, then it will start on localhost and port: 4567
     *
     * @param path       The path of the new server location
     * @param jsonObject The jsonObjecxt to be added to the response json
     */
    public static void addGetToLocalServer(String path, JsonTestObject jsonObject) {
        get(path, FORMAT, (request, response) -> jsonObject,
                new LocalServerJsonTransformer());
    }

    /**
     * Add a new post rest method on a new server location/path to the localserver, expects a JsonTestObject to be posted
     * <p>
     * NOTE: If server is not started before calling this function, then it will start on localhost and port: 4567
     *
     * @param path The path of the new server location
     */
    public static void addPostToLocalServer(String path) {
        post(path, FORMAT, (request, response) -> {
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                postedObject = gson.fromJson(request.body(), JsonTestObject.class);
            } catch (JsonParseException e) {
                logger.error("Could not parse incoming json");
                response.status(500);
            }
            return postedObject;
        });
    }

    /**
     * Returns the previously posted JsonTestObject. Returns null if nothing has been posted.
     *
     * @return The previously psoted JsonTestObject
     */
    public static JsonTestObject getPostedJsonTestObject() {
        return postedObject;
    }

    /**
     * Stops the local server instance if it is running
     */
    public static void stopLocalServer() {
        stop();
    }

    private static String requestInfoToString(Request request) {
        String sb = request.requestMethod() +
                " " + request.url() +
                " " + request.body();
        return sb;
    }
}
