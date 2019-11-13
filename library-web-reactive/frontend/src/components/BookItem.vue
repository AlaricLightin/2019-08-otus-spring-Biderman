<template>
    <div>
        <b-list-group-item>
            <b-row>
                <b-col>
                    <p>{{authorsAndTitleString}}</p>
                    <small>{{genreString}}</small>
                </b-col>
                <b-col cols="3">
                    <b-button variant="warning" v-on:click="startEdit">Редактировать</b-button>
                    <b-button variant="danger" v-on:click="deleteBook">Удалить</b-button>
                </b-col>
            </b-row>
        </b-list-group-item>

        <b-modal id="bookEditModal" title="Редактирование книги" v-model="editMode" v-on:ok="saveEdited">
            <BookEdit :book="editingBook"/>
        </b-modal>
    </div>
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
                editMode: false,
                editingBook: null
            }
        },

        computed: {
            ...mapGetters({
                getAuthorById: "authors/getById"
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
                return this.book.genres.join(', ')
            }
        },
        
        methods: {
            startEdit: function () {
                this.editingBook = ({
                    id: this.book.id,
                    authors: [...this.book.authors],
                    title: this.book.title,
                    genres: [...this.book.genres]
                });
                this.editMode = true;
            },

            saveEdited: function() {
                this.$store.dispatch('books/saveBook', this.editingBook);
            },

            deleteBook: function () {
                this.$store.dispatch("books/deleteBook", this.book.id);
            }
        }
        
    }
</script>

<style scoped>

</style>