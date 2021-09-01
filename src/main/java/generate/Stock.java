package generate;

import java.io.Serializable;
import lombok.Data;

/**
 * stock
 * @author 
 */
@Data
public class Stock implements Serializable {
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 库存
     */
    private Integer count;

    /**
     * 已售
     */
    private Integer sale;

    /**
     * 乐观锁，版本号
     */
    private Integer version;

    private static final long serialVersionUID = 1L;
}