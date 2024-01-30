// package top.maishuren.filter;
//
// import com.msr.better.common.constants.TraceLogConstant;
// import com.msr.better.common.util.TraceIdUtil;
// import org.apache.commons.lang3.StringUtils;
// import org.apache.dubbo.common.constants.CommonConstants;
// import org.apache.dubbo.common.extension.Activate;
// import org.apache.dubbo.rpc.Filter;
// import org.apache.dubbo.rpc.Invocation;
// import org.apache.dubbo.rpc.Invoker;
// import org.apache.dubbo.rpc.Result;
// import org.apache.dubbo.rpc.RpcException;
// import org.slf4j.MDC;
//
// /**
//  * @author maisrcn@qq.com
//  * @since 2024-01-30
//  */
// @Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER})
// public class TraceLogDubboFilter implements Filter {
//
//     @Override
//     public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
//         // 获取dubbo上下文中的traceId
//         String traceId = invocation.getAttachment(TraceLogConstant.TRACE_ID);
//         if (StringUtils.isEmpty(traceId)) {
//             // customer 获取上游来的traceId，并设置到dubbo的上下文，如果没有则生成一个
//             traceId = MDC.get(TraceLogConstant.TRACE_ID);
//             if (StringUtils.isEmpty(traceId)) {
//                 traceId = TraceIdUtil.uuid_timestamp();
//                 MDC.put(TraceLogConstant.TRACE_ID, traceId);
//             }
//             // provider 设置traceId到日志到上下文
//             invocation.setAttachment(TraceLogConstant.TRACE_ID, traceId);
//         } else {
//             MDC.put(TraceLogConstant.TRACE_ID, traceId);
//         }
//         Result result = invoker.invoke(invocation);
//         return result;
//     }
// }
