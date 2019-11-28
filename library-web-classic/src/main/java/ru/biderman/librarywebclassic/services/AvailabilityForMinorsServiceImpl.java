package ru.biderman.librarywebclassic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import ru.biderman.librarywebclassic.domain.Book;

import java.util.Collections;

@Service("availabilityForMinorsService")
@RequiredArgsConstructor
public class AvailabilityForMinorsServiceImpl implements AvailabilityForMinorsService {
    private final AclService aclService;
    private final MutableAclService mutableAclService;

    private static Sid admin = new GrantedAuthoritySid("admin");
    private static Sid user = new GrantedAuthoritySid("ROLE_USER");
    private static Sid adult = new GrantedAuthoritySid("ROLE_ADULT");

    private boolean isAdultOnly(Acl acl) {
        try {
            return !acl.isGranted(
                Collections.singletonList(BasePermission.READ),
                Collections.singletonList(user),
                false);
        }
        catch (NotFoundException e) {
            return true;
        }
    }

    @Override
    public boolean isAdultOnly(Book book) {
        try {
            Acl acl = aclService.readAclById(new ObjectIdentityImpl(book));
            return isAdultOnly(acl);
        }
        catch (NotFoundException e) {
            return false;
        }
    }

    private void addAcl(ObjectIdentity objectIdentity, boolean adultOnly) {
        mutableAclService.deleteAcl(objectIdentity, false);
        MutableAcl acl = mutableAclService.createAcl(objectIdentity);
        acl.setOwner(admin); // Для простоты у нас книги сейчас создаёт только админ
        acl.insertAce(acl.getEntries().size(),
                BasePermission.READ,
                adultOnly ? adult : user,
                true);

        mutableAclService.updateAcl(acl);
    }

    @Override
    public void setRights(Book book, boolean adultOnly) {
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(book);
        try {
            Acl acl = aclService.readAclById(objectIdentity);
            if (isAdultOnly(acl) != adultOnly) {
                addAcl(objectIdentity, adultOnly);
            }
        }
        catch (NotFoundException e) {
            addAcl(objectIdentity, adultOnly);
        }
    }
}
