package org.changsol.utils.bases.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass //자식에게 매핑정보만 제공
public class ChangSolBaseDomainIdentity extends ChangSolBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
}
