
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>${pageTitle}</title>

        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/style.css" rel="stylesheet">

        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">

        <!--Google font-->
        <!--<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">-->
    </head>

    <body class="bg-light">

        <div class="container-fluid">
            <div class="row">

                <!-- SIDEBAR -->
                <jsp:include page="adminSidebar.jsp" />

                <!-- MAIN CONTENT -->
                <div class="col-md-9 col-lg-10 p-0 bg-light content-col">
                    <div class="content-scroll p-4">
                        <jsp:include page="${contentPage}" />
                    </div>
                </div>


            </div>
        </div>


        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>