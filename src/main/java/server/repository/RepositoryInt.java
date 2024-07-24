package server.repository;

import java.io.IOException;

public interface RepositoryInt {

    String uploadLog() throws IOException;

    void safeLog(String log) throws IOException;

}
