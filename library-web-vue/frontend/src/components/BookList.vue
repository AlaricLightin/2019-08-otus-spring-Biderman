<template>
    <div class="m-2">
        <h1>Книги</h1>
        <b-list-group>
            <BookItem v-for="book in this.books" :key="book.id" :book="book"/>
        </b-list-group>

        <div v-if="!editMode" class="m-2">
            <b-button variant="primary" v-on:click="setEditMode(true)">Добавить</b-button>
        </div>
        <div v-else>
            <BookEdit :book="createNewBook()" :title="'Добавление книги'" v-on:finishEdit="setEditMode(false)"></BookEdit>
        </div>
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
                editMode: false
            }
        },

        computed: mapState({
            books: state => state.books.all
        }),

        mounted() {
            this.$store.dispatch('books/load');
            this.$store.dispatch('authors/load');
            this.$store.dispatch('genres/load');
        },

        methods: {
            setEditMode: function (editMode) {
                this.editMode = editMode;
            },

            createNewBook: function () {
                return {id: 0, authors: [null], title: '', genres: []}
            }
        }
    }
</script>

<style scoped>

</style>