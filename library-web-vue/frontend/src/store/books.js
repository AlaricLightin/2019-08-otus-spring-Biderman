import axios from 'axios'

const state = {
    all: []
};

const getters = {
    getIdx: (state) => (id) => {
        return state.all.findIndex(b => b.id === id);
    }
};

const mutations = {
    SET_BOOKS (state, books) {
        state.all = books
    },

    UPDATE_BOOK(state, payload) {
        let oldBook = state.all[payload.idx];
        const newBook = payload.newBook;
        oldBook.authors = [...newBook.authors];
        oldBook.title = newBook.title;
        oldBook.genres = [...newBook.genres];
    },

    ADD_BOOK(state, newBook) {
        state.all.push(newBook);
    },

    DELETE_BOOK(state, idx) {
        state.all.splice(idx, 1);
    }
};

const actions = {
    load({commit}) {
        axios
            .get('/book')
            .then(r => r.data)
            .then(books => {
                commit('SET_BOOKS', books)
            })
    },

    saveBook({commit, getters}, book) {
        if(book.id) {
            axios
                .put('/book/' + book.id, book)
                .then(() => {
                    const idx = getters.getIdx(book.id);
                    commit('UPDATE_BOOK', { idx: idx, newBook: book });
                });
        }
        else {
            axios.post('/book', book)
                .then(r => {
                    const location = r.headers['location'];
                    const idx = location.lastIndexOf('/');
                    book.id = location.substr(idx + 1);
                    commit('ADD_BOOK', book);
                });
        }
    },

    deleteBook({commit, getters}, bookId) {
        axios.delete('/book/' + bookId)
            .then(() => {
                const idx = getters.getIdx(bookId);
                commit('DELETE_BOOK', idx);
            });
    }
};

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}