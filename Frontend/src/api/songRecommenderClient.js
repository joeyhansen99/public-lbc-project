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
export default class SongRecommenderClient  extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getSampleSongs', 'createSong'];
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
    async getSampleSongs(errorCallback) {
        try {
            const response = await this.client.post(`/songs/sampleSongs`);
            return response.data;
        } catch (error) {
            this.handleError("getSampleSongs", error, errorCallback)
        }
    }

    /**
     * Gets the song for the given ID.
     * @param id Unique identifier for a song
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The song
     */
    async getRandomSong(videoLength, errorCallback) {
        try {
            const response = await this.client.get(`/songs/random/${videoLength}`);
            return response.data;
        } catch (error) {
            this.handleError("getRandomSong", error, errorCallback)
        }
    }

    async createSong(videoLength, errorCallback) {
        try {
            const response = await this.client.get(`/songs`, {
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
