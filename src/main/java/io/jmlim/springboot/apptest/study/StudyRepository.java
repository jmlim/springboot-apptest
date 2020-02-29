package io.jmlim.springboot.apptest.study;

import io.jmlim.springboot.apptest.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository  extends JpaRepository<Study, Long> {
}
