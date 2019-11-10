<template>
    <b-form>
        <h4>{{title}}</h4>
        <div class="p-1 m-1">
            <b-form-group label="Автор:" v-for="(author, index) in newBook.authors" :key="index">
                <b-form-select v-model="newBook.authors[index]" :options="authorsOptions">
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
                <b-form-input id="input-title" required v-model="newBook.title"></b-form-input>
            </b-form-group>
        </div>

        <div class="p-1 m-1">
            <b-form-group label="Жанры:">
                <b-form-select id="select-genres" multiple v-model="newBook.genres" :options="genresOptions" :select-size="5"/>
            </b-form-group>
        </div>

        <div class="p-1 m-1">
            <b-button variant="primary" v-on:click="save">Сохранить</b-button>
            <b-button variant="danger" v-on:click="finishEdit">Отмена</b-button>
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

        data: function() {
            return {
                newBook: ({
                    id: this.book.id,
                    authors: [...this.book.authors],
                    title: this.book.title,
                    genres: [...this.book.genres]
                })
            }
        },
        
        computed: {
            ...mapState({
                allGenres: state => state.genres.all,
                allAuthors: state => state.authors.all
            }),

            genresOptions: function () {
                return this.allGenres.map(genre => ({value: genre.id, text: genre.text}))
            },

            authorsOptions: function () {
                return this.allAuthors.map(author => ({value: author.id, text: author.otherNames + ' ' + author.surname}))
            }
        },

        methods: {
            save: function() {
                this.newBook.authors = this.newBook.authors.filter(s => s);
                this.$store.dispatch('books/saveBook', this.newBook);
                this.$emit('finishEdit');
            },

            finishEdit: function () {
                this.$emit('finishEdit');
            },

            addAuthor: function () {
                this.newBook.authors.push(null);
            },

            deleteAuthor: function () {
                this.newBook.authors.pop();
            }
        }
    }
</script>

<style scoped>

</style>