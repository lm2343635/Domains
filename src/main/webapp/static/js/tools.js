function checkAdminSession(done) {
    AdminManager.checkSession(function (username) {
        if (username == null) {
            sessionError();
            return;
        }
        done(username);
    });
}

function checkEmployeeSession(done) {
    EmployeeManager.checkSession(function (employee) {
        if (employee == null) {
            sessionError();
            return;
        }
        done(employee);
    });
}

function sessionError() {
    var redirect = encodeURIComponent(location.href);
    location.href = "session.html?redirect=" + redirect;
}

function linkError() {
    var redirect = encodeURIComponent(location.href);
    location.href = "link.html?redirect=" + redirect;
}

var RolePrevilgeNone = 0;
var RolePrevilgeAssign = 1;
var RolePrevilgeHold = 2;

var CustomerStateUndeveloped = 0;
var CustomerStateDeveloping = 1;
var CustomerStateDeveloped = 2;
var CustomerStateLost = 3;

var TypeCategoryExpiration = 0;
var TypeCategoryReport = 1;
var TypeCategoryDocument = 2;

var CustomerStateNames = ["未开发", "待开发", "已开发", "已流失"];
var TypeCategoryNames = ["到期", "报告", "文档"];

if ($.messager != undefined) {
    $.messager.model = {
        ok: {
            text: "确定",
            classed: "btn-danger"
        },
        cancel: {
            text: "取消",
            classed: "btn-default"
        }
    };
}

var BROSWER_PC = "pc";
var BROSWER_WAP = "wap";

//“年-月-日”格式
var YEAR_MONTH_DATE_FORMAT = "yyyy-MM-dd";
//“年-月”格式
var YEAR_MONTH_FORMAT = "yyyy-MM";
//“年”格式
var YEAR_FORMAT = "yyyy";
//“年-月-日 时:分”格式
var DATE_HOUR_FORMAT = "yyyy-MM-dd hh";
//“年-月-日 时:分”格式
var DATE_HOUR_MINUTE_FORMAT = "yyyy-MM-dd hh:mm";
//“年-月-日 时:分:秒”格式
var DATE_HOUR_MINUTE_SECOND_FORMAT = "yyyy-MM-dd hh:mm:ss";

//“年-月-日”格式 中文
var YEAR_MONTH_DATE_FORMAT_CN = "yyyy年MM月dd日";
//“年-月-日 时”格式 中文
var DATE_HOUR_FORMAT_CN = "yyyy年MM月dd日 hh时";
//“年-月-日 时:分:秒”格式 中文
var DATE_HOUR_MINUTE_FORMAT_CN = "yyyy年MM月dd日 hh点mm分";

//summernote完整toolbar
var SUMMERNOTE_TOOLBAR_TEXT_ONLY = [
    ['style', ['style']],
    ['font', ['bold', 'italic', 'underline', 'clear', 'strikethrough']],
    ['fontname', ['fontname']],
    ['fontsize', ['fontsize']],
    ['color', ['color']],
    ['para', ['ul', 'ol', 'paragraph']],
    ['height', ['height']],
    ['table', ['table']],
    ['insert', ['link']],
    ['view', ['fullscreen', 'codeview', 'help']]
];

//summernote禁用图片视频toolbar
var SUMMERNOTE_TOOLBAR_FULL = [
    ['style', ['style']],
    ['font', ['bold', 'italic', 'underline', 'clear', 'strikethrough']],
    ['fontname', ['fontname']],
    ['fontsize', ['fontsize']],
    ['color', ['color']],
    ['para', ['ul', 'ol', 'paragraph']],
    ['height', ['height']],
    ['table', ['table']],
    ['insert', ['link', 'picture', 'video']],
    ['view', ['fullscreen', 'codeview', 'help']]
];

/**
 * 交换元素位置
 * @param a
 * @param b
 */
function exchangeElement(a, b) {
    var n = a.next(), p = b.prev();
    b.insertBefore(n);
    a.insertAfter(p);
};

/**
 * 判断是否为数字
 * @param num
 * @returns
 */
function isNum(num) {
    var patten = new RegExp(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/);
    return patten.test(num);
}

/**
 * 判断是否为整数
 * @param num
 * @returns
 */
function isInteger(num) {
    var patten = new RegExp(/^-?[1-9]\d*$/);
    return patten.test(num);
}

/**
 * 判断是否为电子邮件格式
 * @param email
 * @returns
 */
function isEmailAddress(email) {
    var patten = new RegExp(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/);
    return patten.test(email);
}

/**
 * 格式化时间
 * @param format 时间格式
 * @returns
 */
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(),    //day
        "h+": this.getHours(),   //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
        "S": this.getMilliseconds() //millisecond
    };
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
};

/**
 * 得到每个月的天数
 * @param year
 * @return
 */
function getMonthDay(year) {
    var monthDay = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    if (isLeapYear(year))
        monthDay[1]++;
    return monthDay;
}

/**
 * 判断是否闰年
 * @param year
 * @returns {Boolean}
 */
function isLeapYear(year) {
    return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
}

/**
 * 得到两个日期之间的日期差
 * @param start
 * @param end
 */
function getDaysBetweenDates(start, end) {
    return Math.floor((end.getTime() - start.getTime()) / (24 * 3600 * 1000));
}

/**
 * 得到两个日期之间的分钟差
 * @param start
 * @param end
 */
function getMinutesBetweenDates(start, end) {
    return Math.floor((end.getTime() - start.getTime()) / (60 * 1000));
}

/**
 * 闪烁提醒
 * @param selector 选择器
 * @param time 闪烁时间，单位毫秒
 * @param frequency 闪烁频率，单位毫秒
 */
function flickerTip(selector, time, frequency) {
    var flicker = setInterval(function () {
        if ($(selector).css("visibility") == "hidden")
            $(selector).css("visibility", "inherit");
        else
            $(selector).css("visibility", "hidden");
    }, frequency);
    setTimeout(function () {
        clearInterval(flicker);
        $(selector).css("visibility", "inherit");
    }, time);
}

/**
 * 接收request参数
 * @param paras
 * @returns
 */
function request(paras) {
    var search = window.location.search;
    var paraString = search.substring(search.indexOf("?") + 1, search.length).split("&");
    var paraObj = {}
    for (i = 0; j = paraString[i]; i++)
        paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
    var returnValue = paraObj[paras.toLowerCase()];
    if (typeof(returnValue) == "undefined") {
        return "";
    }
    return returnValue;
}


/**
 * 获取以今天为起始日的第next日
 * @param next
 * @returns
 */
function nextDay(next) {
    var date = new Date();
    date.setDate(date.getDate() + next);
    return date;
}

/**
 * 得到本月起始日期
 * @returns {String}
 */
function getThisMonthStart() {
    var nowMonth = new Date().format("yyyy-MM");
    return nowMonth + "-01";
}

/**
 * 得到本月结束日期
 * @returns {String}
 */
function getThisMonthEnd() {
    var nowMonth = new Date().format("yyyy-MM");
    var year = parseInt(nowMonth.split("-")[0]);
    var month = parseInt(nowMonth.split("-")[1]);
    var monthDay = getMonthDay(year);
    return nowMonth + "-" + monthDay[month - 1];
}

/**
 * 得到本年起始日期
 * @returns {String}
 */
function getThisYearStart() {
    var nowYear = new Date().format("yyyy");
    return nowYear + "-01-01";
}


/**
 * 得到本年结束
 * @returns {String}
 */
function getThisYearEnd() {
    var nowYear = new Date().format("yyyy");
    return nowYear + "-12-31";
}

/**
 * 只保留一个字符串中的前n个字符
 * @param str
 * @param n
 * @returns
 */
function substr(str, n) {
    if (str.length > n) {
        return str.substr(0, n) + "...";
    } else {
        return str;
    }
}

/**
 * 判断是否为移动站点
 * @returns {Boolean}
 */
function isMobile() {
    var urlhash = window.location.hash;
    if (!urlhash.match("fromapp")) {
        if ((navigator.userAgent.match(/(iPhone|iPod|Android|ios|iPad)/i))) {
            return true;
        }
    }
    return false;
}

/**
 * 获取浏览器类型
 * @returns
 */
function getBroswerType() {
    return isMobile() ? BROSWER_WAP : BROSWER_PC;
}

/**
 * 设置页面描述
 * @param content
 */
function setPageDescription(content) {
    var meta = document.getElementsByTagName("meta");
    for (i in meta) {
        if (typeof meta[i].name != "undefined" && meta[i].name.toLowerCase() == "description") {
            meta[i].content = content;
        }
    }
}

/**
 * 得到页面全名
 * @returns
 */
function getPageFullName() {
    var array = window.location.pathname.split("/");
    return array[array.length - 1];
}

/**
 * 得到页面名称
 * @returns
 */
function getPageName() {
    return getPageFullName().split(".")[0];
}

String.prototype.getBytesLength = function () {
    return this.replace(/[^\x00-\xff]/gi, "--").length;
}

String.prototype.getBytesLengthString = function () {
    var length = this.replace(/[^\x00-\xff]/gi, "--").length;
    return formatByte(length);
}

function formatByte(length) {
    if (length > 1024) {
        length /= 1024;
        return parseInt(length) + "KB";
    }
    return length + "B";
}