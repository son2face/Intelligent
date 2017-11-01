package Module.File;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.util.List;

@Path("/files")
public class FileController {
    @Inject
    private FileService fileService;

    public FileController() {

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<FileEntity> get(@BeanParam SearchFileModel searchFileModel) {
        return fileService.get(searchFileModel);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/Count")
    public int count(@BeanParam SearchFileModel searchFileModel) {
        return 100;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{fileId}")
    public FileEntity getId(@PathParam("fileId") int fileId) {
        return fileService.get(fileId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public FileEntity create(FileEntity fileEntity) {
        FileEntity FileEntity = fileService.create(fileEntity);
        FileEntity.data = "";
        return FileEntity;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{fileId}")
    public FileEntity update(@PathParam("fileId") int fileId, FileEntity fileEntity) {
        return fileService.update(fileId, fileEntity);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{fileId}")
    public void delete(@PathParam("fileId") int fileId) {
        fileService.delete(fileId);
    }

    @GET
    @Path("{fileId}/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("fileId") int fileID) {
        FileEntity fileEntity = fileService.download(fileID);
        byte[] contents = org.infinispan.commons.util.Base64.decode(fileEntity.data);
        StreamingOutput stream = os -> {
            os.write(contents);
            os.flush();
        };
        return Response
                .ok(stream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition", "attachment; filename=" + fileEntity.name)
                .build();
    }
}