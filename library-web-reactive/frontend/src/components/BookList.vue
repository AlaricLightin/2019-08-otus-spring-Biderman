<template>
    <div class="m-2">
        <h1>Книги</h1>
        <b-list-group>
            <BookItem v-for="book in this.books" :key="book.id" :book="book"/>
        </b-list-group>

        <div class="m-2">
            <b-button variant="primary" v-on:click="startEdit">Добавить</b-button>
        </div>

        <b-modal id="bookCreateModal" title="Добавление книги" v-model="editMode" v-on:ok="saveEdited">
            <BookEdit :book="editingBook"/>
        </b-modal>
    </div>
</template>

<script>
    import BookItem from "./BookItem";
    import BookEdit from "./BookEdit";
    import {mapState} from 'vuex';

    export default {
        name: "BookList",
        components: {BookEdit, BookItem},

        data: function() {
            return {
                editMode: false,
                editingBook: null
            }
        },

        computed: mapState({
            books: state => state.books.all
        }),

        mounted() {
            this.$store.dispatch('books/load');
            this.$store.dispatch('authors/load');
            this.$store.dispatch('books/loadGenres');
        },

        methods: {
            startEdit: function () {
                this.editingBook = {id: 0, authors: [null], title: '', genres: []}; 
                this.editMode = true;
            },
            
            saveEdited: function () {
                this.$store.dispatch('books/saveBook', this.editingBook);
            }
        }
    }
</script>

<style scoped>

</style>