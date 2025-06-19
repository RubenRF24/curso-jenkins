
create table tbl_cliente (
    id_cliente bigint not null auto_increment,
    nombre varchar(100) not null,
    correo varchar(100) not null,
    telefono varchar(20) null,
    primary key (id_cliente)
);

create table tbl_producto (
    id_producto bigint not null auto_increment,
    nombre varchar(100) not null,
    precio double not null,
    categoria varchar(100) not null,
    stock int(11) not null,
    primary key (id_producto)
);

create table tbl_pedido (
    id_pedido bigint not null auto_increment,
    id_cliente bigint not null not null,
    estado varchar(50) not null,
    primary key (id_pedido)
);

create table tbl_detallespedido (
    id_detallesPedido bigint not null auto_increment,
    id_pedido bigint not null,
    id_producto bigint not null,
    cantidad int(11) not null,
    primary key (id_detallesPedido)
);


ALTER TABLE tbl_pedido ADD FOREIGN KEY (id_cliente) REFERENCES tbl_cliente(id_cliente) on update cascade on delete cascade;
ALTER TABLE tbl_detallespedido ADD FOREIGN KEY (id_pedido) REFERENCES tbl_pedido(id_pedido) on update cascade on delete cascade;
ALTER TABLE tbl_detallespedido ADD FOREIGN KEY (id_producto) REFERENCES tbl_producto(id_producto) on update cascade on delete cascade;