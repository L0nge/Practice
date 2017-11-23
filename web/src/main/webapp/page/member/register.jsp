<%@ page pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/common/jsp/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <meta name="format-detection" content="telephone=no">
    <title>登录</title>
    <link rel="stylesheet" href="${ctx}/page/member/css/register.css" media="screen" title="no title" charset="utf-8">
</head>
<body>
<div class="header">
    <h1>用户登录</h1>
</div>

<!-- 登录内容 -->
<div class="login-box">
    <!-- 选项卡切换 -->
    <div class="tabs">
        <ul id="tabs">
            <li>快速注册</li>
            <li class="actived">登录</li>
        </ul>
    </div>

    <!-- 切换内容 -->
    <ul id="tab-content">
        <!-- 注册 -->
        <li class="reg-box" data-flag="reg1">
            <form>
                <div class="phone-num">
                    <i class="iconfont">&#xe61a;</i><input id="memMobile" name="memMobile" placeholder="填写手机号">
                </div>
                <div class="code-num">
                    <i class="iconfont">&#xe607;</i><input id="code" name="code" placeholder="输入验证码">
                    <button type="button" name="button" id="sendCode">点击获取</button>
                </div>
                <div class="login-pw">
                    <i class="iconfont">&#xe61b;</i><input type="password" id="memPassWord" name="memPassWord" placeholder="输入登录密码">
                </div>
                <div class="submit-btn">
                    <button type="button" name="button" onclick="regeditMem()">快速注册</button>
                </div>
            </form>
        </li>

        <!-- 登录 -->
        <li class="log-box">
            <form>
                <div class="phone-num">
                    <i class="iconfont">&#xe61a;</i><input name="memMobilel" id="memMobilel" placeholder="手机号" value="">
                </div>
                <div class="login-pw">
                    <i class="iconfont">&#xe61b;</i><input name="memPassWordl" id="memPassWordl" type="password" placeholder="密码">
                </div>
                <div class="forget-pw">
                    <a href="javascript:void(0)" onclick="forgetPassword()">忘记密码</a>
                </div>
                <!-- 提交按钮 -->
                <div class="submit-btn">
                    <button type="button" name="button" onclick="memLogin()">登录</button>
                </div>
            </form>

            <div class="other-login" id="weixin" style="display:none">
                <p>第三方登录</p>
                <a href=""><i class="iconfont">&#xe62a;</i></a>
            </div>
        </li>
    </ul>
</div>
<!-- 错误弹窗 -->
<div class="error-box" id="error_box">
    <p>${msg}</p>
    <p id="back_close" onclick="backClose()">返回确认</p>
</div>
<!-- 遮罩 -->
<div class="mask-cover" id="mask-cover"></div>
<c:if test="${!empty msg}">
    <!-- 错误弹窗 -->
    <div class="error-boxs" id="error_boxs">
        <p>${msg}</p>
        <p id="back_closes" onclick="backCloses()">返回确认</p>
    </div>
    <!-- 遮罩 -->
    <div class="mask-covers" id="mask-covers"></div>
</c:if>

<script src="${ctx}/common/js/rem.js" charset="utf-8"></script>
<script src="${ctx}/common/js/jquery-2.1.4.min.js" charset="utf-8"></script>
<script type="text/javascript">
    $(function() {
        $("#tabs li").each(function(i){
            $("#tabs li").eq(i).click(function(){
                $(this).addClass("actived");
                $(this).siblings().removeClass("actived");
                $("#tab-content li").eq(i).show();
                $("#tab-content li").eq(i).siblings().hide();
            })
        });

        // 登录错误时,显示错误弹窗信息
        function errorBox() {

            var $mask_cover =  $("#mask-cover"),
                $error_box = $("#error_box");

            // 显示遮罩
            $mask_cover.show();

            // 显示错误提示
            $error_box.show();

            // 启用关闭弹窗
            $("#error_box p:last-child").click(function(){
                $error_box.hide();
                $mask_cover.hide();
            })
        }
    });

    $(document).ready(function() {
        if(is_weixin()){
            $("#weixin").attr("style","display:none");
            /*2017.02.28屏蔽微信快捷登录  */
        }
    });

    function is_weixin(){
        var ua = navigator.userAgent.toLowerCase();
        if(ua.match(/MicroMessenger/i)=="micromessenger") {
            return true;
        } else {
            return false;
        }
    }


    function sendcode(){
        var memMobile=$("#memMobile").val();
        $.ajax({
            url : "${ctx}/code/sendRcode.jhtml",
            type : "POST",
            dataType : "JSON",
            data : {
                "memMobile" : memMobile,
            },
            success : function(result){
                console.log(result);
            }
        });
    }


    $(function () {
        var wait_time = 60;
        function timeLimit(o) {
            if (wait_time == 0) {
                o.removeAttribute("disabled");
                o.style.backgroundColor = "#e50053"
                o.innerHTML="获取验证码";
                wait_time = 60;
            } else {
                o.setAttribute("disabled", true);
                o.style.backgroundColor = "#9e9e9e"
                o.innerHTML="重新发送(" + wait_time + ")";
                wait_time--;
                setTimeout(function() {
                        timeLimit(o)
                    },
                    1000)
            }
        }
        document.getElementById("sendCode").onclick = function () {if(checkRegCode()){timeLimit(this); sendcode();}}
    });


    function memLogin(){
        if(checkLogin()){
            var form = document.forms[1];
            var url=window.location.href;
            var urlTemp=url.split("?")[1];
            if(url.indexOf("?")>0){
                form.action = "${ctx}/member/memlogincenter.jhtml?"+urlTemp;
            }else{
                form.action = "${ctx}/member/memlogincenter.jhtml";
            }
            form.method = "post";
            form.submit();
        }
    }

    function regeditMem(){
        if(checkReg()){
            var form = document.forms[0];
            form.action = "${ctx}/member/regeditMem.jhtml";
            form.method = "post";
            form.submit();
        }
    }

    function checkRegCode() {
        if ($("#memMobile").val() == "") {
            $(".mask-cover").show();
            $(".error-box").html("<p>手机号码不能为空！</p><p id=\"back_close\" onclick=\"backClose()\">返回确认</p>")
            $(".error-box").show();
            return false;
        }

        /* if (!$("#memMobile").val().match(/^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/)) {
            $(".mask-cover").show();
            $(".error-box").html("<p>手机号码格式不正确！</p><p id=\"back_close\" onclick=\"backClose()\">返回确认</p>")
            $(".error-box").show();
            $("#mobile").focus();
            return false;
        } */
        return true;
    }


    function checkReg() {
        if ($("#memMobile").val() == "") {
            $(".mask-cover").show();
            $(".error-box").html("<p>手机号码不能为空！</p><p id=\"back_close\" onclick=\"backClose()\">返回确认</p>")
            $(".error-box").show();
            return false;
        }

        /* if (!$("#memMobile").val().match(/^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/)) {
            $(".mask-cover").show();
            $(".error-box").html("<p>手机号码格式不正确！</p><p id=\"back_close\" onclick=\"backClose()\">返回确认</p>")
            $(".error-box").show();
            $("#mobile").focus();
            return false;
        }  */

        if ($("#code").val() == "") {
            $(".mask-cover").show();
            $(".error-box").html("<p>验证码不能为空!</p><p id=\"back_close\" onclick=\"backClose()\">返回确认</p>")
            $(".error-box").show();
            return false;
        }

        if ($("#memPassWord").val()== "") {
            $(".mask-cover").show();
            $(".error-box").html("<p>密码不能为空！</p><p id=\"back_close\" onclick=\"backClose()\">返回确认</p>")
            $(".error-box").show();
            return false;
        }else{
            if (!$("#memPassWord").val().match(/^(\w){6,16}$/)) {
                $(".mask-cover").show();
                $(".error-box").html("<p>密码输入错误!</p><p id=\"back_close\" onclick=\"backClose()\">返回确认</p>")
                $(".error-box").show();
                return false;
            }
        }
        return true;
    }

    function checkLogin() {
        if ($("#memMobilel").val() == "") {
            $(".mask-cover").show();
            $(".error-box").html("<p>用户不能为空！</p><p id=\"back_close\" onclick=\"backClose()\">返回确认</p>")
            $(".error-box").show();
            return false;
        }

        /* if (!$("#memMobilel").val().match(/^(((13[0-9]{1})|159|153)+\d{8})$/)) {
            $(".mask-cover").show();
            $(".error-box").html("<p>该用户不存在！</p><p id=\"back_close\" onclick=\"backClose()\">返回确认</p>")
            $(".error-box").show();
            $("#mobile").focus();
            return false;
        } */


        if ($("#memPassWordl").val()== "") {
            $(".mask-cover").show();
            $(".error-box").html("<p>密码不能为空！</p><p id=\"back_close\" onclick=\"backClose()\">返回确认</p>")
            $(".error-box").show();
            return false;
        }
        return true;
    }

    function backClose(){
        $(".mask-cover").hide();
        $(".error-box").hide();
        /*$("#memMobile").val("");
        $("#code").val("");
        $("#memPassWord").val("");
        $("#memMobilel").val("");
        $("#memPassWordl").val(""); */
    }

    function backCloses(){
        $(".mask-covers").hide();
        $(".error-boxs").hide();
        /*$("#memMobile").val("");
        $("#code").val("");
        $("#memPassWord").val("");
        $("#memMobilel").val("");
        $("#memPassWordl").val(""); */
    }

    function getCookie(cookie_name){
        var allcookies = document.cookie;
        var cookie_pos = allcookies.indexOf(cookie_name);   //索引的长度
        if (cookie_pos != -1){
            // 把cookie_pos放在值的开始，只要给值加1即可。
            cookie_pos += cookie_name.length + 1;      //这里我自己试过，容易出问题，所以请大家参考的时候自己好好研究一下。。。
            var cookie_end = allcookies.indexOf(";", cookie_pos);
            if (cookie_end == -1)
            {
                cookie_end = allcookies.length;
            }
            var value = unescape(allcookies.substring(cookie_pos, cookie_end)); //这里就可以得到你想要的cookie的值了。。。
        }
        return value;
    }

    function forgetPassword() {
        $('#forgetPassword').submit();

    }
</script>
</body>
</html>

