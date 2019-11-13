module.exports = {
    outputDir: 'target/dist',
    assetsDir: 'static',

    devServer: {
        proxy: 'http://localhost:8080'
    }
};