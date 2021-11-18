package com.youwu.shouyinxitong.bean_used;

import com.youwu.shouyinxitong.bean_used.PemissionBean;

import java.util.List;

public class StoreBean {
    /**
     * store_id : 1
     * wxpay : {"secret_key":"a6f301f601bca0f43ce2eb6de295ae49","cert_path":"","mch_id":"1513138421","app_id":"wx319f7d298047cf89"}
     * alipay : {"alipay_app_id":"2018092961490871","private_key":"MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCFoei0NhTMny6KSO2x6IS0F2zGhle1JvsZ30sH/R25bF7wjkf/VWeqtTQMLjdo8m0FjlwUWjN0NhI7Z/t6JDLIAGEdb9lrACijWW3qS6yR7xbNmBQEmZWl1oMFC0xGeI6ITLCnq4E4ljh+eDsv0bOQlIchAVDDKuo5usMVys46NwP417vx7JunaBhHFOsADmT3VV8ZRXywzGE/696+WJh6Z7fyReTg1S22UcdBOETmS87GAv5IeUmTf2+9xXbMV/iSCMGqViw6hIrbFRX5X9Ebm5yTTM3n2Cj0LQoHl2zGSmEt1Ai4n6HevO0oAtQa8GkKN9x6a3H8ioi+cV0m1N9DAgMBAAECggEAZSq5ME1NG8hbBIfHo4uDg4/EYttr5RH4wZKBjC+85Ba+HWGp8gznKZ57I8mNW19E2BL5dNMA5zbUQLMs+EAVTcKzwwRdVhtNqLTYeKsHn4eg/M7oQL9BQsaOdDAkc6lrRpbAmn9FmeBoLp65OC+KwKchUgRb7xU7M69HZ+UckYbhLZhdUSdnD2jo+to2vKGrxRkDtjtKI4Hy3dzgVWsljBreKWzCu1AVCNOMRWSKaNNDGzKOpDFgQjSXdJARKJ7fSelzs/EEXWfK4Se1C3rHj3GLk1qHn8YBhFkA6AtLQ4BavmWxOTNaJ0ycql26uFz+FmaxxTZkHNt8tVr+CL9LyQKBgQDOpIiBvasVAwjzwZKvqW96WBi4G5Xjg/a3gd3FWner3iWuXeEw7Y77sJy0JP0NUlnOyicdvyzue1vTuzLiaAry7BzXvkH3EkBeY+pdCI7cPcWjG60e7ch468y6Si91QhVZjYvQbd8YdXcakKfIVYOX3jNo+9ipxnmiOtDWE3eeVQKBgQCljQ79OpqXk8iEj5BkZ8ufnG5fWMqQ+4cdU5SI8SZIEgl2+tsOVosPfm0wVIO+R/WsjHJTSOuT3nQ/Vys3WmjVXqGB3Sc9hN9Qoq5XSBFz8xVDfUH1BhM10UnLCVnlCl4EdZVFqAA8qXtMWcRfAJfaIJBci1+y9k8uPYnxBY9vNwKBgCJkyEOv6fXJV4gaw+tw7BkU6KhcDwqbQEky2FKsyBZXFzZ6cazPHWlO8AYb0fKRrlj41KHi2/ullJcdJU8BujDMk9Bk3l3dL9GGF8/8SKrnrSmExb/MG/3kYWLwTg3aPet137uFTgioJHbLdOGnOgvg8/WZVWqMmloiJ/g5rUMxAoGAY5hoExollNkYgf9oi68SBlTy5bOiMfTQRWlZ68TAFg2b2hNyo76W7FxPzgqaM2hROVxJDyfIae4jm1OSsTtX5D2JBnqIt+7u9jDYl7PIpCvyd+asPn9IIVFmhpp6lVtWUnJ2ujAm7Fe90KIUjF7r/BMpJjPHAABEhHoLOY/xyaMCgYA3t8N7OJoGqjVRwYD2kFrdlwTJ2Zg4hnQuvsihlGZPYam+JD0cTmdBvICSCShk83gbUXoHuF8QQpwOx65MSbGsoszwhToCZmf9dGdSBbuUMP5ifzF/OOCIvaRg+lIuhGfybYd1LsrpsUPl3O+0zXnDHMa0nNrLwWUyUkzwWSpXpg==","alipay_public_key":"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApzv2z/25KFrHg4BeZyQvsA8Zj2aRgpbXdHdRWoOFAONd/W0i0UBGMiyuoVA7t4WVXWxwUtOO4nUYJWY30mKSBRF1NXbGyjB18nidWEAClPPtziEcMiv6jckz1yt1RcO+IjPMxm3y2CdCXNdwpmKGyCuKSssoZNE2aFeJUnFRp1+oQbiSsW6ecm5+nY4iPFDEyP3DMpMqryjIDjH/MZUgOAQNZBd9XqlfqZshLb53tdzUvhgAIUYB4dxrFG2KqTYCTxbYbEZbUhkvbzpYj0gQATgmq4LaafIrVJgqiwXn+AfU9UMjJar2RBcMqYWAVQg3+ywS3cdcu894QObsNRv8LQIDAQAB"}
     * store_name : 有物
     * kitchens : [{"kitchen_name":"厨房1","kitchen_id":"0"}]
     * token : bfd35ee4-7e70-4177-9f99-e820cb100dcc
     */


    private String store_id;
    private WxpayBean wxpay;
    private AlipayBean alipay;
    private String code_url;
    private String phone;
    private String account_name;
    private String description;
    private String logo;
    private String store_address;
    private String store_name;
    private String id;
    private String token;
    private String staff_name;             //收银员名称
    private String electronicInvoices_url; //发票二维码路径前缀，需在后方拼接order_sn
    private List<PemissionBean> menu_data;

    public List<PemissionBean> getMenu_data() {
        return menu_data;
    }

    public void setMenu_data(List<PemissionBean> menu_data) {
        this.menu_data = menu_data;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getElectronicInvoices_url() {
        return electronicInvoices_url;
    }

    public void setElectronicInvoices_url(String electronicInvoices_url) {
        this.electronicInvoices_url = electronicInvoices_url;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public WxpayBean getWxpay() {
        return wxpay;
    }

    public void setWxpay(WxpayBean wxpay) {
        this.wxpay = wxpay;
    }

    public AlipayBean getAlipay() {
        return alipay;
    }

    public void setAlipay(AlipayBean alipay) {
        this.alipay = alipay;
    }

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class WxpayBean {
        /**
         * secret_key : a6f301f601bca0f43ce2eb6de295ae49
         * cert_path :
         * mch_id : 1513138421
         * app_id : wx319f7d298047cf89
         */

        private String secret_key;
        private String cert_path;
        private String mch_id;
        private String app_id;

        public String getSecret_key() {
            return secret_key;
        }

        public void setSecret_key(String secret_key) {
            this.secret_key = secret_key;
        }

        public String getCert_path() {
            return cert_path;
        }

        public void setCert_path(String cert_path) {
            this.cert_path = cert_path;
        }

        public String getMch_id() {
            return mch_id;
        }

        public void setMch_id(String mch_id) {
            this.mch_id = mch_id;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }
    }

    public static class AlipayBean {
        /**
         * alipay_app_id : 2018092961490871
         * private_key : MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCFoei0NhTMny6KSO2x6IS0F2zGhle1JvsZ30sH/R25bF7wjkf/VWeqtTQMLjdo8m0FjlwUWjN0NhI7Z/t6JDLIAGEdb9lrACijWW3qS6yR7xbNmBQEmZWl1oMFC0xGeI6ITLCnq4E4ljh+eDsv0bOQlIchAVDDKuo5usMVys46NwP417vx7JunaBhHFOsADmT3VV8ZRXywzGE/696+WJh6Z7fyReTg1S22UcdBOETmS87GAv5IeUmTf2+9xXbMV/iSCMGqViw6hIrbFRX5X9Ebm5yTTM3n2Cj0LQoHl2zGSmEt1Ai4n6HevO0oAtQa8GkKN9x6a3H8ioi+cV0m1N9DAgMBAAECggEAZSq5ME1NG8hbBIfHo4uDg4/EYttr5RH4wZKBjC+85Ba+HWGp8gznKZ57I8mNW19E2BL5dNMA5zbUQLMs+EAVTcKzwwRdVhtNqLTYeKsHn4eg/M7oQL9BQsaOdDAkc6lrRpbAmn9FmeBoLp65OC+KwKchUgRb7xU7M69HZ+UckYbhLZhdUSdnD2jo+to2vKGrxRkDtjtKI4Hy3dzgVWsljBreKWzCu1AVCNOMRWSKaNNDGzKOpDFgQjSXdJARKJ7fSelzs/EEXWfK4Se1C3rHj3GLk1qHn8YBhFkA6AtLQ4BavmWxOTNaJ0ycql26uFz+FmaxxTZkHNt8tVr+CL9LyQKBgQDOpIiBvasVAwjzwZKvqW96WBi4G5Xjg/a3gd3FWner3iWuXeEw7Y77sJy0JP0NUlnOyicdvyzue1vTuzLiaAry7BzXvkH3EkBeY+pdCI7cPcWjG60e7ch468y6Si91QhVZjYvQbd8YdXcakKfIVYOX3jNo+9ipxnmiOtDWE3eeVQKBgQCljQ79OpqXk8iEj5BkZ8ufnG5fWMqQ+4cdU5SI8SZIEgl2+tsOVosPfm0wVIO+R/WsjHJTSOuT3nQ/Vys3WmjVXqGB3Sc9hN9Qoq5XSBFz8xVDfUH1BhM10UnLCVnlCl4EdZVFqAA8qXtMWcRfAJfaIJBci1+y9k8uPYnxBY9vNwKBgCJkyEOv6fXJV4gaw+tw7BkU6KhcDwqbQEky2FKsyBZXFzZ6cazPHWlO8AYb0fKRrlj41KHi2/ullJcdJU8BujDMk9Bk3l3dL9GGF8/8SKrnrSmExb/MG/3kYWLwTg3aPet137uFTgioJHbLdOGnOgvg8/WZVWqMmloiJ/g5rUMxAoGAY5hoExollNkYgf9oi68SBlTy5bOiMfTQRWlZ68TAFg2b2hNyo76W7FxPzgqaM2hROVxJDyfIae4jm1OSsTtX5D2JBnqIt+7u9jDYl7PIpCvyd+asPn9IIVFmhpp6lVtWUnJ2ujAm7Fe90KIUjF7r/BMpJjPHAABEhHoLOY/xyaMCgYA3t8N7OJoGqjVRwYD2kFrdlwTJ2Zg4hnQuvsihlGZPYam+JD0cTmdBvICSCShk83gbUXoHuF8QQpwOx65MSbGsoszwhToCZmf9dGdSBbuUMP5ifzF/OOCIvaRg+lIuhGfybYd1LsrpsUPl3O+0zXnDHMa0nNrLwWUyUkzwWSpXpg==
         * alipay_public_key : MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApzv2z/25KFrHg4BeZyQvsA8Zj2aRgpbXdHdRWoOFAONd/W0i0UBGMiyuoVA7t4WVXWxwUtOO4nUYJWY30mKSBRF1NXbGyjB18nidWEAClPPtziEcMiv6jckz1yt1RcO+IjPMxm3y2CdCXNdwpmKGyCuKSssoZNE2aFeJUnFRp1+oQbiSsW6ecm5+nY4iPFDEyP3DMpMqryjIDjH/MZUgOAQNZBd9XqlfqZshLb53tdzUvhgAIUYB4dxrFG2KqTYCTxbYbEZbUhkvbzpYj0gQATgmq4LaafIrVJgqiwXn+AfU9UMjJar2RBcMqYWAVQg3+ywS3cdcu894QObsNRv8LQIDAQAB
         */

        private String alipay_app_id;
        private String private_key;
        private String alipay_public_key;

        public String getAlipay_app_id() {
            return alipay_app_id;
        }

        public void setAlipay_app_id(String alipay_app_id) {
            this.alipay_app_id = alipay_app_id;
        }

        public String getPrivate_key() {
            return private_key;
        }

        public void setPrivate_key(String private_key) {
            this.private_key = private_key;
        }

        public String getAlipay_public_key() {
            return alipay_public_key;
        }

        public void setAlipay_public_key(String alipay_public_key) {
            this.alipay_public_key = alipay_public_key;
        }
    }


}
