<template>
    <b-form>
        <div class="p-1 m-1">
            <b-form-group label="Автор:" v-for="(author, index) in book.authors" :key="index">
                <b-form-select v-model="book.authors[index]" :options="authorsOptions">
                    <template v-slot:first>
                        <option :value="null" disabled>Выберите автора</option>
                    </template>
                </b-form-select>
            </b-form-group>
            <div>
                <b-button variant="primary" size="sm" v-on:click="addAuthor">Добавить автора</b-button>
                <b-button variant="danger" size="sm" v-on:click="deleteAuthor">Удалить автора</b-button>
            </div>
        </div>

        <div class="p-1 m-1">
            <b-form-group label="Название книги:">
                <b-form-input id="input-title" required v-model="book.title"></b-form-input>
            </b-form-group>
        </div>

        <div class="p-1 m-1">
            <b-form-group label="Жанр:" v-for="(genre, index) in book.genres" :key="index">
                <b-form-input v-model="book.genres[index]" list="genreList"/>
            </b-form-group>
            <b-form-datalist id="genreList" :options="this.allGenres"></b-form-datalist>
            <div>
                <b-button variant="primary" size="sm" v-on:click="addGenre">Добавить жанр</b-button>
                <b-button variant="danger" size="sm" v-on:click="deleteGenre">Удалить жанр</b-button>
            </div>
        </div>
    </b-form>
</template>

<script>
    import {mapState} from 'vuex'

    export default {
        name: "BookEdit",
        props: {
            book: Object,
            title: String
        },

        computed: {
            ...mapState({
                allGenres: state => state.books.allGenres,
                allAuthors: state => state.authors.all
            }),

            authorsOptions: function () {
                return this.allAuthors.map(author => ({value: author.id, text: author.otherNames + ' ' + author.surname}))
            }
        },

        methods: {
            addAuthor: function () {
                this.book.authors.push(null);
            },

            deleteAuthor: function () {
                this.book.authors.pop();
            },

            addGenre: function () {
                this.book.genres.push("");
            },

            deleteGenre: function () {
                this.book.genres.pop();
            }
        }
    }
</script>

<style scoped>

</style>