package com.example.shop2.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1721019202L;

    public static final QMember member = new QMember("member1");

    public final com.example.shop2.domain.QBaseTimeEntity _super = new com.example.shop2.domain.QBaseTimeEntity(this);

    public final StringPath address = createString("address");

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final StringPath email = createString("email");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DatePath<java.time.LocalDate> modifiedTime = _super.modifiedTime;

    public final StringPath name = createString("name");

    public final ListPath<Order, QOrder> order = this.<Order, QOrder>createList("order", Order.class, QOrder.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    //inherited
    public final DatePath<java.time.LocalDate> regTime = _super.regTime;

    public final EnumPath<com.example.shop2.domain.constant.Role> role = createEnum("role", com.example.shop2.domain.constant.Role.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

