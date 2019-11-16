import Vue from 'vue';
import Vuex from 'vuex';
import authors from "./authors";
import books from "./books";
import axios from 'axios';
import VueAxios from 'vue-axios'

Vue.use(Vuex);
Vue.use(VueAxios, axios);

export default new Vuex.Store({
    modules: {
        authors,
        books
    }
})
