package com.manulife.id.service;

import com.manulife.id.constant.ResponseCode;
import com.manulife.id.dto.ImageRequestDto;
import com.manulife.id.dto.RequestPaging;
import com.manulife.id.dto.ResponsePagingDto;
import com.manulife.id.dto.UserProfileDto;
import com.manulife.id.exception.BadRequestException;
import com.manulife.id.factory.UserProfileFactory;
import com.manulife.id.model.MasterUser;
import com.manulife.id.repository.UserRepository;
import com.manulife.id.util.Base64Utils;
import com.manulife.id.util.PDFGenerator;
import com.manulife.id.util.ResponseUtil;
import com.manulife.id.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserProfileService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserProfileFactory factory;

    public UserProfileDto create(UserProfileDto request, HttpServletRequest servletRequest) {
        // validation
        if ( !StringUtil.isValidEmail(request.getEmail()) )
            throw new BadRequestException("Email is not valid", ResponseCode.EMAIL_IS_NOD_VALID);

        boolean isEmailExist = repository.existsByEmailAndIsDeletedFalse(request.getEmail());
        if ( isEmailExist ) throw new BadRequestException("Email is already used", ResponseCode.EMAIL_ALREADY_USED);

        boolean isUsernameExist = repository.existsByUsernameAndIsDeletedFalse(request.getUsername());
        if (isUsernameExist) throw new BadRequestException("Username is already used", ResponseCode.USERNAME_ALREADY_USED);

        // saving
        MasterUser entity = factory.fillEntity(request, new MasterUser());
        repository.save(entity);
        return factory.buildDto(entity);
    }

    @Transactional
    public UserProfileDto update(UserProfileDto request, HttpServletRequest servletRequest) {

        MasterUser entity = repository.findByUsernameAndIsDeletedFalse(request.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found", ResponseCode.GENERAL_NOT_FOUND));
        // validation
        if ( !StringUtil.isValidEmail(request.getEmail()) )
            throw new BadRequestException("Email is not valid", ResponseCode.EMAIL_IS_NOD_VALID);

        if (!request.getEmail().equals(entity.getEmail())){
            boolean isEmailExist = repository.existsByEmailAndIsDeletedFalse(request.getEmail());
            if ( isEmailExist ) throw new BadRequestException("Email is already used", ResponseCode.EMAIL_ALREADY_USED);
        }

        // saving
        factory.fillEntity(request, entity);
        repository.save(entity);

        return factory.buildDto(entity);

    }

    @Transactional
    public void uploadImage(ImageRequestDto request, HttpServletRequest servletRequest) {

        MasterUser entity = repository.findByUsernameAndIsDeletedFalse(request.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found", ResponseCode.GENERAL_NOT_FOUND));

        byte[] byteImage = Base64Utils.decodeBase64ToImage(request.getBase64());;
        Base64Utils.validateImage(byteImage);

        entity.setImage(byteImage);
        repository.save(entity);
    }

    @Transactional
    public void delete(String username, HttpServletRequest servletRequest) {
        MasterUser entity = repository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new BadRequestException("User not found", ResponseCode.GENERAL_NOT_FOUND));
        entity.setIsDeleted(true);
        repository.save(entity);
    }

    public List<UserProfileDto> get(String username, HttpServletRequest servletRequest) {
        List<MasterUser> listEntity =  new ArrayList<>();
        if (username == null || username.trim().isEmpty() ){
            listEntity = repository.findAllByIsDeletedFalse();
        }else {
            MasterUser entity = repository.findByUsernameAndIsDeletedFalse(username)
                    .orElseThrow(() -> new BadRequestException("User not found", ResponseCode.GENERAL_NOT_FOUND));
            listEntity.add(entity);
        }
        return listEntity.stream().map(x -> factory.buildDto(x)).collect(Collectors.toList());
    }
    public ResponsePagingDto<List<UserProfileDto>> getPaging(RequestPaging request, HttpServletRequest servletRequest) {
        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
        Page<MasterUser> pageData = repository.findAllByIsDeletedFalse(pageable);
        List<UserProfileDto> listDto = pageData.getContent().stream().map(x -> factory.buildDto(x)).collect(Collectors.toList());
        return ResponseUtil.paging(listDto, pageData);
    }

    public byte[] getImage(String username, HttpServletRequest servletRequest) {
        MasterUser entity = repository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new BadRequestException("User not found", ResponseCode.GENERAL_NOT_FOUND));
        byte[] image = entity.getImage();
        if (image == null) {
            throw new BadRequestException("No image found for user", ResponseCode.GENERAL_NOT_FOUND);
        }
        return image;
    }

    public ByteArrayInputStream exportPdf(HttpServletRequest servletRequest) {
        String template = "/templates/SimpleReport.jrxml";

        List<MasterUser> listEntity = repository.findAllByIsDeletedFalse();
        Map<String, Object> data = new HashMap<>();

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listEntity);
        return PDFGenerator.generateJasperFile(template,data, dataSource);
    }

}
