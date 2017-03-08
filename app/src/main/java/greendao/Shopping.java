package greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

/**
 * Created by 情v枫 on 2017/3/8.
 * <p>
 * 作用：
 */
@Entity
public class Shopping {

    //id
    @Id(autoincrement = true)
    private Long id;
    //商品名称
    @Unique
    private String name;
    @Generated(hash = 58076793)
    public Shopping(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1564781055)
    public Shopping() {
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
