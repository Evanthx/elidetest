<#-- @ftlvariable name="" type="com.usermind.elidetest.swagger.SwaggerView" -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="icon" type="image/png" href="${swaggerAssetsPath}/images/favicon-32x32.png" sizes="32x32" />
    <link rel="icon" type="image/png" href="${swaggerAssetsPath}/images/favicon-16x16.png" sizes="16x16" />
    <link rel="stylesheet" href="${swaggerAssetsPath}/css/theme.css">
    <link href='http://cdn.jsdelivr.net/webjars/org.webjars.bower/swagger-ui-themes/2.0.0/themes/theme-muted.css' media='screen' rel='stylesheet' type='text/css'/>

    <link href='${swaggerAssetsPath}/css/typography.css' media='screen' rel='stylesheet' type='text/css'/>
    <link href='${swaggerAssetsPath}/css/reset.css' media='screen' rel='stylesheet' type='text/css'/>
    <link href='${swaggerAssetsPath}/css/screen.css' media='screen' rel='stylesheet' type='text/css'/>
    <link href='${swaggerAssetsPath}/css/reset.css' media='print' rel='stylesheet' type='text/css'/>
    <link href='${swaggerAssetsPath}/css/print.css' media='print' rel='stylesheet' type='text/css'/>

    <script src='${swaggerAssetsPath}/lib/object-assign-pollyfill.js' type='text/javascript'></script>
    <script src='${swaggerAssetsPath}/lib/jquery-1.8.0.min.js' type='text/javascript'></script>
    <script src='${swaggerAssetsPath}/lib/jquery.slideto.min.js' type='text/javascript'></script>
    <script src='${swaggerAssetsPath}/lib/jquery.wiggle.min.js' type='text/javascript'></script>
    <script src='${swaggerAssetsPath}/lib/jquery.ba-bbq.min.js' type='text/javascript'></script>
    <script src='${swaggerAssetsPath}/lib/handlebars-4.0.5.js' type='text/javascript'></script>
    <script src='${swaggerAssetsPath}/lib/lodash.min.js' type='text/javascript'></script>
    <script src='${swaggerAssetsPath}/lib/backbone-min.js' type='text/javascript'></script>
    <script src='${swaggerAssetsPath}/swagger-ui.js' type='text/javascript'></script>
    <script src='${swaggerAssetsPath}/lib/highlight.9.1.0.pack.js' type='text/javascript'></script>
    <script src='${swaggerAssetsPath}/lib/highlight.9.1.0.pack_extended.js' type='text/javascript'></script>
    <script src='${swaggerAssetsPath}/lib/jsoneditor.min.js' type='text/javascript'></script>
    <script src='${swaggerAssetsPath}/lib/marked.js' type='text/javascript'></script>
    <script src='${swaggerAssetsPath}/lib/swagger-oauth.js' type='text/javascript'></script>

    <script type="text/javascript">
        $(function () {
            hljs.configure({
                highlightSizeThreshold: 5000
            });

            // Pre load translate...
            if(window.SwaggerTranslator) {
                window.SwaggerTranslator.translate();
            }
            window.swaggerUi = new SwaggerUi({
                url: "${contextPath}/swagger.json",

                dom_id: "swagger-ui-container",
                supportedSubmitMethods: ['get', 'post', 'put', 'delete', 'patch'],
                onComplete: function(swaggerApi, swaggerUi){


                    if(window.SwaggerTranslator) {
                        window.SwaggerTranslator.translate();
                    }
                },
                onFailure: function(data) {
                    log("Unable to Load SwaggerUI");
                },
                docExpansion: "none",
                jsonEditor: false,
                apisSorter: "alpha",
                defaultModelRendering: 'schema',
                showRequestHeaders: false
            });

            window.swaggerUi.load();

            function log() {
                if ('console' in window) {
                    console.log.apply(console, arguments);
                }
            }
        });
    </script>
<body class="swagger-section">
<div id='header' >
    <div class="swagger-ui-wrap">
        <table width="100%">
            <tr>
                <td>
                    <a href="https://usermind.com/" >
                        <img border="0"  src="${swaggerAssetsPath}/images/logo.png" width="100" height="30" >
                    </a>
                </td>
                <td>
                    <p><font face="verdana" color="green">Welcome to the Usermindsdk Project!</font></p>
                </td>
            </tr>
            <table>
    </div>
</div>


<h4><Operation Layer</h4>
<div id="swagger-ui-container" class="swagger-ui-wrap">
</div>
<div id="swagger-ui-footer" align="right">
    <p>
        <a class="rawbutton" href="swagger.json" target="_blank">json</a>
    </p>

</body>
</html>

