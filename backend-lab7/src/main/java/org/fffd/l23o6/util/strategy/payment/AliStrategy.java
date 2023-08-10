package org.fffd.l23o6.util.strategy.payment;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import org.fffd.l23o6.pojo.entity.OrderEntity;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;

import java.io.File;

import static com.alipay.easysdk.kernel.util.ResponseChecker.*;


/**
 * @program: l23o6
 * @description:
 * @author: Xie
 * @create: 2023-06-28 16:35
 **/

public class AliStrategy extends PaymentStrategy {
    static String app_id = "9021000122697804";
    static String private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDQVGAj3w/ml68p4nyGf0ihwbj32gevQNtQFpeKlW3vdwNgmczgnkK9/dwEvE7xXu1W9wXAJrVZbEbzDQ+jramBy4lyAw3x1xiIPJjZhMXpJ+xhuxrBNcmmeDU5TWPbIBq4lN3uSONnN6c1zlYzX8/T1A2YgOdOF/HzmGvv9WU0W3qRELWeyR5m18ExH2l+DeGvZ0MH3GR8BbslGQv3FIOrJexRd/gDDbD5yZ3RRBReh0BPWoguzDI1xU1Nf6Fe/+CYEcINvgdNOZer1HHyhLNqSxdv1Y2IMYxp95NCiZn0/UzEw+RC0XeGziBMQNtYKRYD1eiY7JUSPngtqIw2Kvg1AgMBAAECggEAcy/z6GGRXSJJS5Na3tLJygZey0vWK+RkXqffKlTs8BaR23uueBOJO5t9Z3DnBTZDbG4w08mTQ9pGcGSsXQfMTWPIny1pLGqNueYnbT2oX/4fCPp3FJU05cJZS1GrAKGDpMNK4lGSyqNO5MUtjEJRmwIIcmXzfo5mzRVduNOqHYPnIi/6YygbwtdREEUxulH8hzCGoZmddYDNfUzgXjmqHKF9iaeBDfRF88nrKBdx81/jlDKP+2kJw2ThWcbbw3F3Er/w8Hye5Fr6e03pPvbfjqsl8KIOc2wpoMk5oP+ZcvKKaC9hShYM+IdxifBA/yzJ7Hxg4c16oX49+J8RgZABgQKBgQD185yLXnxE3/06oXNrBv67oAly82nqZp+Neixvv0Dsk6FsVcdATTmAIgtCdJ4SBWJqRUeHCtvQCdPMMwHKIb+dSi5sAvnW34mgVH1ark+YqtsSPzShx0QWT0Mwob+ZWL8WmxguWXJx0YIhc+zdq8ujb6ZgSQeigbk/kwhhMxJZOQKBgQDY10dB01CsHyYeoHhZsG1mtXAyWa4x+O0YSfNEggVS8Veb6McNLxuhdIbHTD1hFNQUJwZX011mLnCUo9umgRB8sngt8HIvdfQ/o+t520e83CE6tuNWI5cYBdRF2DJYpeEk/X0vgCN8ogVq7TorZpSaDEVd/h+x/+I508ImFy2C3QKBgDdy140YHR4N/XIVagbsCcvU9axjsjI1Nq1XqOL8lC+BzPe2kvgtMNRxWGaAEg7wBdL9kXWhfhL11DkKGtIuL5vPWI3jUd4egVGTGrux5qH9iuwfMcdzPCdncTdqj96LgnEfQCFa6xE78YGHOBR1obBUAIdej4kUg+YQSOhY8j8JAoGAIWydqUHfb4iloZbutjSByJEzKC+2sDFEqrfLbcX0+CIHdRreg+O9CJgjpljUJUDGqAyT57nG7dc+t0rCstm2JuKDr+EeWL5/1cd4pqHsXuo3nQtP45k80xEi9Vkr+YooOEFyqH3B+pYm66KBkusPAOffUWu4iwF8Z5mClciA1WkCgYEAvbB1i6ihaXMCTv+y4S8Sky3M2HR4Z62SUBoUtv9S2+jzrP7YfCiNaJsDslKBqObz9BKScO16t39XoAeGgYDN1EYBy/uL5CbHcvbKm4sb0wcU/++HwBLtEE2GjweHeTp9QMw2zpKyYndNjWBflWvH8i1N9BzShB9ZnmPrtRceoJI=";
    static String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp7dGigKO+DxCFjfNbFqQkgD161tyjTh4iBsw3adBBjeVvJylsxISY7k+DlZ1e0hTIQOrd3xWdfDaue6XaV17m1qVREfLKPf6zi3BJc4yQQthgGJMNzbZ+YmqnAsElVjyGpWlpHS1N+22q+bX4bdIe1h0WzAyMhLE8nZbGKMTdwJKEIhGCf71VPWhazeVo6WUAiyCwehI6luftpR9SJUbNF/tJFBuxVIa0nOWvUg2Gsp2ZkLwNOnAW6epSnUdnQXJl0uWnZ1VXbA5RJhplSkAMAhJTH1MgOstFehU+1l4dB5ltSMKhSaq7310/wlxT618L/g+mOagGgLfPC60w4G+YwIDAQAB";
    @Override
    public boolean pay(OrderEntity order) {
        String subject = order.getUserId() + "-" + order.getTrainId();
        String totalAmount = Double.toString(order.getMoney());
        String traceNo = Long.toString(order.getId());
        AlipayTradePagePayResponse response;
        try {
            // 发起API调用（以创建当面付收款二维码为例）
            response = Factory.Payment.Page()
                    .pay(subject, traceNo, totalAmount, "");
            System.out.println("Yes");
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return success(response);
    }
    @Override
    public boolean refund(OrderEntity order) {
        double totalAmount = order.getMoney();
        String traceNo = Long.toString(order.getId());
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi-sandbox.dl.alipaydev.com/gateway.do", app_id, private_key,"json","utf-8", alipayPublicKey,"RSA2");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("refund_amount", totalAmount);
        bizContent.put("out_request_no", traceNo);
        request.setBizContent(bizContent.toString());
        AlipayTradeRefundResponse response;
        try {
            response = alipayClient.execute(request);

            return true;
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    public static Config getOptions() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipaydev.com";
        config.signType = "RSA2";
        config.appId = "9021000122697804";
        config.merchantPrivateKey = private_key;
        config.alipayPublicKey = alipayPublicKey;
        return config;
    }


}
