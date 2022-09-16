import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import SongRecommenderClient from "../api/songRecommenderClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class SongRecommenderPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGetSample', 'onGetRandomSong', 'renderSong'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('create-sample-song-form').addEventListener('submit', this.onGetSample);
        document.getElementById('get-random-song-form').addEventListener('submit', this.onGetRandomSong);
        this.client = new SongRecommenderClient();
        this.dataStore.addChangeListener(this.renderSong)
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderSong() {
        let resultArea = document.getElementById("songArea");


        const randomSongs = this.dataStore.get("randomSong");

        let fullHTML = "";

        if(randomSongs){
            for (let song of randomSongs) {
                // use the comment in the HTML
                fullHTML +=
                    `<ul>
                    <li>
                        <h3>${song.title}</h3>
                    </li>
                    <h4>By: ${song.artist}</h4>
                    <h4>Song Length: ${song.songLength}</h4>
                </ul>`;
            }
            resultArea.innerHTML = fullHTML;
        } else {
            resultArea.innerHTML = "No Item";
        }
    }


    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGetSample(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let id = document.getElementById("create-sample-song-form").value;
        this.dataStore.set("songSamples", null);

        let result = await this.client.getSampleSongs(this.errorHandler);
        this.dataStore.set("songSamples", result);
        if (result) {
            this.showMessage(`Got sample list!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async onGetRandomSong(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("randomSong", null);

        let videoLength = document.getElementById("get-random-song-field").value;

        const randomSong = await this.client.getRandomSong(videoLength, this.errorHandler);
        this.dataStore.set("randomSong", randomSong);

        if (randomSong) {
            this.showMessage(`Created ${randomSong.name}!`)
        } else {
            this.errorHandler("Error creating!  Try again...");
        }
    }
    async onGetSongs() {
    let result = await this.client.getAllSongs(this.errorHandler)
    this.dataStore.set("songs", result)
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const songRecommenderPage = new SongRecommenderPage();
    songRecommenderPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
