const path = require("path");
const webpack = require("webpack");
const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {
    mode: 'production',
    entry: {
        app: "./app.js"
    },
    output: {
        path: __dirname + "/src/main/resources/static/",
        filename: "[name].bundle.js"
    },
    plugins: [
        new CopyWebpackPlugin([
            {from: './node_modules/jquery/dist', to: './modules/jquery'},
            {from: './node_modules/bootstrap/dist', to: './modules/bootstrap'},
            {from: './node_modules/bootstrap-datepicker/dist', to: './modules/bootstrap-datepicker'},
            {from: './node_modules/bootstrap-daterangepicker', to: './modules/bootstrap-daterangepicker'},
            {from: './node_modules/fastclick', to: './modules/fastclick'},
            {from: './node_modules/font-awesome', to: './modules/font-awesome'},
            {from: './node_modules/ionicons/dist', to: './modules/ionicons'},
            {from: './node_modules/select2/dist', to: './modules/select2'},
            {from: './node_modules/icheck', to: './modules/icheck'},
            {from: './node_modules/js-sha512/build', to: './modules/js-sha512'},
            {from: './node_modules/admin-lte/dist', to: './modules/admin-lte'},
            {from: './node_modules/admin-lte/plugins/jQueryUI', to: './modules/jquery-ui'},
            {from: './node_modules/jquery-form/dist', to: './modules/jquery-form'},
            {from: './node_modules/jstree/dist', to: './modules/jstree'},
            {from: './node_modules/tinymce', to: './modules/tinymce'},
            {from: './node_modules/moment', to: './modules/moment'},
            {from: './node_modules/datatables.net-bs/css', to: './modules/datatables.net-bs/css'},
            {from: './node_modules/datatables.net-bs/js', to: './modules/datatables.net-bs/js'},
            {from: './node_modules/datatables.net/js', to: './modules/datatables.net/js'},
            {from: './node_modules/datatables.net-select/js', to: './modules/datatables.net-select/js'},
            {from: './node_modules/jquery-slimscroll/jquery.slimscroll.min.js', to: './modules/jquery-slimscroll/jquery.slimscroll.min.js'}
        ])
    ]
};
