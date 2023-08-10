package helloworld.service.imp;

import helloworld.dto.EmailStructureDTO;
import helloworld.enums.TypeMail;

public interface EmailResolver {

    boolean type(TypeMail type);

    void send(EmailStructureDTO emailStructureDTO);

}
