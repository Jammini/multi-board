package study.multiproject.file.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.multiproject.file.domain.UploadFile;

@Repository
public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

}
