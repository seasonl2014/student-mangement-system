package cn.xueden.student.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：系统用户实体类
 * @author:梁志杰
 * @date:2022/12/2
 * @description:cn.xueden.student.domain
 * @version:1.0
 */
@Data
@Entity
@Table(name = "sys_user")
@org.hibernate.annotations.Table(appliesTo = "sys_user",comment="系统用户信息表")
public class SysUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;

    @Column(name="realname")
    private String realname;

    @Column(name="sex")
    private String sex;

    @Column(name = "status")
    private Integer status;

    @Column(name = "email")
    private String email;

    @Column(name = "user_icon")
    private String userIcon;

    /**
     * 所属角色
     */
    @OneToOne
    @JoinColumn(name = "role_id",referencedColumnName="id")
    private SysRole sysRole;
}
