import axios from 'axios'

const state = {
    all: []
};

const getters = {
    getById: (state) => (id) => {
        return state.all.find(genre => genre.id === id)
    }
};

const mutations = {
    SET_GENRES (state, genres) {
        state.all = genres
    }
};

const actions = {
    load({commit}) {
        axios
            .get('/genre')
            .then(r => r.data)
            .then(genres => {
                commit('SET_GENRES', genres)
            })
    }
};

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}