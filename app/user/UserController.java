package user;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import play.libs.Json;
import play.libs.concurrent.ClassLoaderExecutionContext;
import play.mvc.*;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@With(UserAction.class)
public class UserController extends Controller {

    private static final String FILE_NAME = "users.xlsx";
    private static final String TEMP_LOCATION_CONF = "myapp.tmp";
    private static final String SHEET_NAME = "All users";
    private ClassLoaderExecutionContext ec;
    private UserResourceHandler handler;

    private Config config;

    @Inject
    public UserController(ClassLoaderExecutionContext ec, UserResourceHandler handler, Config config) {
        this.ec = ec;
        this.handler = handler;
        this.config = config;
    }

    public CompletionStage<Result> list(Http.Request request) {
        return handler.find(request).thenApplyAsync(posts -> {
            final List<UserResource> postList = posts.collect(Collectors.toList());
            return ok(Json.toJson(postList));
        }, ec.current());
    }

    public CompletionStage<Result> show(Http.Request request, String id) {
        return handler.lookup(request, id).thenApplyAsync(optionalResource -> optionalResource.map(resource ->
            ok(Json.toJson(resource))
        ).orElseGet(Results::notFound), ec.current());
    }

    public CompletionStage<Result> update(Http.Request request, String id) {
        JsonNode json = request.body().asJson();
        UserResource resource = Json.fromJson(json, UserResource.class);
        return handler.update(request, id, resource).thenApplyAsync(optionalResource -> optionalResource.map(r ->
                ok(Json.toJson(r))
        ).orElseGet(Results::notFound
        ), ec.current());
    }

    public CompletionStage<Result> create(Http.Request request) {
        JsonNode json = request.body().asJson();
        final UserResource resource = Json.fromJson(json, UserResource.class);
        return handler.create(request, resource).thenApplyAsync(savedResource -> created(Json.toJson(savedResource)), ec.current());
    }

    public CompletionStage<Result> export(Http.Request request) {
        return handler.find(request).thenApplyAsync(posts -> {
            final List<UserResource> postList = posts.collect(Collectors.toList());
            XSSFWorkbook workbook = writeXls(postList);
            String uploadPath = config.getString(TEMP_LOCATION_CONF);
            Long time = System.currentTimeMillis();
            String filePath = new StringBuilder(uploadPath).append(File.separator).append(time).append(FILE_NAME).toString();
            try {
                FileOutputStream fos = new FileOutputStream(filePath);
                workbook.write(fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            java.io.File file = new java.io.File(filePath);
            return ok(file, true);
        }, ec.current());
    }

    private XSSFWorkbook writeXls(List<UserResource> postList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(SHEET_NAME);

        int rowCount = 0;
        for (UserResource post : postList) {
            Row row = sheet.createRow(++rowCount);
            int columnCount = 0;
            Cell cell1 = row.createCell(++columnCount);
            cell1.setCellValue(post.getName());

            Cell cell2 = row.createCell(++columnCount);
            cell2.setCellValue(post.getEmail());

            Cell cell3 = row.createCell(++columnCount);
            cell3.setCellValue(post.getLink());
        }

        return workbook;
    }
}
