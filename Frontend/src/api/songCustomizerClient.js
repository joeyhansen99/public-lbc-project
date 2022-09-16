import BaseClass from "../util/baseClass";
import axios from 'axios'

/**
 * Client to call the SongController.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class SongCustomizerClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getSong', 'createSong'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    async getAllSongs(errorCallback) {
        try {
            const response = await this.client.get(`/songs`);
            return response.data;
        } catch (error) {
            this.handleError("getAllSongs", error, errorCallback)
        }
    }

    /**
     * Gets the concert for the given ID.
     * @param id Unique identifier for a concert
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The concert
     */
    async getSong(id, errorCallback) {
        try {
            const response = await this.client.get(`/songs/${id}`);
            return response.data;
        } catch (error) {
            this.handleError("getAllSongs", error, errorCallback)
        }
    }

    async createSong(artist, title, genre, songLength, errorCallback) {
        try {
            const response = await this.client.post(`/songs/`, {
                artist : artist,
                title : title,
                songLength : songLength,
            });
            return response.data;
        } catch (error) {
            this.handleError("createSong", error, errorCallback);
        }
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}
