package cpm.msr.better.elastic.job;

import cpm.msr.better.elastic.domain.Student;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-30 00:00
 **/
@Component
public class SpringBootDataflowJob implements DataflowJob<Student> {
    @Override
    public List<Student> fetchData(ShardingContext shardingContext) {
        // 获取数据
        System.out.println("fetch data:");
        System.out.println(shardingContext.toString());
        Student jack = new Student("Jack", 18);
        Student tom = new Student("tom", 19);
        List<Student> list = new ArrayList<>();
        list.add(jack);
        list.add(tom);
        return list;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<Student> list) {
        // 处理数据
        System.out.println("process data:");
        System.out.println(shardingContext.toString());
        list.forEach(e -> System.out.println(e.toString()));
    }
}
