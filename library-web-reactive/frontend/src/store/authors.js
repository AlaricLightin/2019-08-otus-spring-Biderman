import axios from 'axios'

const state = {
    all: []
};

const getters = {
    getById: (state) => (id) => {
        return state.all.find(author => author.id === id)
    }
};

const mutations = {
    SET_AUTHORS (state, authors) {
        state.all = authors
    }
};

const actions = {
    load({commit}) {
        axios
            .get('/author')
            .then(r => r.data)
            .then(authors => {
                commit('SET_AUTHORS', authors)
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