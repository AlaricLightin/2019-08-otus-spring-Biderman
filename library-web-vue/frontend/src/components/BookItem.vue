<template>
    <b-list-group-item>
        <b-row v-if="!editMode" >
            <b-col>
                <p>{{authorsAndTitleString}}</p>
                <small>{{genreString}}</small>
            </b-col>
            <b-col cols="3">
                <b-button variant="warning" v-on:click="setEditMode(true)">Редактировать</b-button>
                <b-button variant="danger" v-on:click="deleteBook">Удалить</b-button>
            </b-col>
        </b-row>
        <b-row v-else>
            <BookEdit :book="book" :title="'Редактирование книги'" v-on:finishEdit="setEditMode(false)"/>
        </b-row>
    </b-list-group-item>
</template>

<script>
    import {mapGetters} from 'vuex';
    import BookEdit from "./BookEdit";

    export default {
        name: "BookItem",
        components: {BookEdit},
        props: {
            book: Object
        },

        data: function() {
            return {
                editMode: false
            }
        },

        computed: {
            ...mapGetters({
                getAuthorById: "authors/getById",
                getGenreById: "genres/getById"
            }),

            authorsAndTitleString: function () {
                let authorString = this.book.authors
                    .map((id) => this.getAuthorById(id))
                    .filter((object) => object)
                    .map((author) => author.otherNames + ' ' + author.surname)
                    .join(', ');

                let titleString = '"' + this.book.title + '"';

                if(authorString)
                    return authorString + ', ' + titleString;
                else
                    return titleString;
            },

            genreString: function () {
                return this.book.genres
                    .map((id) => this.getGenreById(id))
                    .filter((object) => object)
                    .map((genre) => genre.text)
                    .join(', ')
            }
        },
        
        methods: {
            setEditMode: function (editMode) {
                this.editMode = editMode;
            },

            deleteBook: function () {
                this.$store.dispatch("books/deleteBook", this.book.id);
            }
        }
        
    }
</script>

<style scoped>

</style>