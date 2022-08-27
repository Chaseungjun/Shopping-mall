package com.example.shop2.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCartItem is a Querydsl query type for CartItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartItem extends EntityPathBase<CartItem> {

    private static final long serialVersionUID = 318614391L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCartItem cartItem = new QCartItem("cartItem");

    public final com.example.shop2.domain.QBaseTimeEntity _super = new com.example.shop2.domain.QBaseTimeEntity(this);

    public final QCart cart;

    public final NumberPath<Long> CartItemId = createNumber("CartItemId", Long.class);

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final QItem item;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DatePath<java.time.LocalDate> modifiedTime = _super.modifiedTime;

    //inherited
    public final DatePath<java.time.LocalDate> regTime = _super.regTime;

    public QCartItem(String variable) {
        this(CartItem.class, forVariable(variable), INITS);
    }

    public QCartItem(Path<? extends CartItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCartItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCartItem(PathMetadata metadata, PathInits inits) {
        this(CartItem.class, metadata, inits);
    }

    public QCartItem(Class<? extends CartItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cart = inits.isInitialized("cart") ? new QCart(forProperty("cart"), inits.get("cart")) : null;
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item")) : null;
    }

}
