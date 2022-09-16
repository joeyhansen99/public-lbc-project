import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import SongCustomizerClient from "../api/songCustomizerClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class SongCustomizerPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'onCreate', 'renderSong'], this);
        this.bindClassMethods(['onGetSongs', 'renderSongs'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('get-song-form').addEventListener('submit', this.onGet);
        document.getElementById('create-song-form').addEventListener('submit', this.onCreate);
        document.getElementById('get-songs-form').addEventListener('submit', this.onGetSongs);

        this.client = new SongCustomizerClient();
        this.dataStore.addChangeListener(this.renderSong)
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderSong() {
        let resultArea = document.getElementById("songs");

        const song = this.dataStore.get("song");

        if (song) {
            resultArea.innerHTML = `
                <div>Title: ${song.title}</div>
                <div>Artist: ${song.artist}</div>
            `;
        } else {
            resultArea.innerHTML = "No Item";
        }
    }
    async renderSongs() {
        let resultArea = document.getElementById("songs");

        const songs = this.dataStore.get("songs");

        let fullHTML = "";

        if(songs){
            for (let song of songs) {
                // use the comment in the HTML
                fullHTML +=
                    `<ul>
                    <li>
                        <h3>${song.title}</h3>
                    </li>
                    <h4>By: ${song.artist}</h4>
                </ul>`;
            }
            resultArea.innerHTML = fullHTML;
        } else {
            resultArea.innerHTML = "No Item";
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGet(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let id = document.getElementById("get-song-field").value;
        this.dataStore.set("song", null);

        let result = await this.client.getSong(id, this.errorHandler);
        this.dataStore.set("song", result);
        if (result) {
            this.showMessage(`Got ${result.name}!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async onCreate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("song", null);

        let title = document.getElementById("create-song-title-field").value;
        let artist = document.getElementById("create-song-artist-field").value;
        let genre = document.getElementById("create-song-genre-field").value;
        let songLength = document.getElementById("create-song-genre-field").value;

        const createdSong = await this.client.createSong(artist, title, genre, songLength, this.errorHandler);
        this.dataStore.set("song", createdSong);

        if (createdSong) {
            this.showMessage(`Created ${createdSong.name}!`)
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
    const songRecommenderPage = new SongCustomizerPage();
    songRecommenderPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
