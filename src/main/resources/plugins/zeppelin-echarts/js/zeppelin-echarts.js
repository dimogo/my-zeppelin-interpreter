Array.prototype.del = function (index) {
    if (isNaN(index) || index >= this.length) {
        return false;
    }
    for (var i = 0, n = 0; i < this.length; i++) {
        if (this[i] != this[index]) {
            this[n++] = this[i];
        }
    }
    this.length -= 1;
}

function buildGraphicOption(option, orginData, paragraphId, serieConfigs) {
    var title = $("#ze-paragraph-" + paragraphId + " #ze-title").val();
    var subtitle = $("#ze-paragraph-" + paragraphId + " #ze-subtitle").val();
    option.title.text = title;
    option.title.subtext = subtitle;
    option.series = buildSerieDataByOrigin(orginData, serieConfigs);
    option.legend.data = buildLegendDataByOption(option);
    setRadarIndicator(option);
    checkOption(option);
    return option;
}

function drawChart(redraw, chart, option) {
    if (redraw) {
        if (chart && chart.dispose) {
            chart.dispose();
        }
        chart = echarts.init(chart, "macarons");
    }
    chart.showLoading({text: "正在重新绘制图表,请稍候..."})
    try {
        chart.setOption(option, true)
        chart.hideLoading();
    } catch (e) {
        chart.showLoading({text:"绘制图表出错了"});
    }
    return chart;
}

function fillOriginDataOptionsFromStruct(data, paragraphId) {
    $("#ze-paragraph-" + paragraphId + " #ze-selected-fields option").remove();
    $("#ze-paragraph-" + paragraphId + " #ze-selectable-fields option").remove();
    for (var key in data) {
        $("#ze-paragraph-" + paragraphId + " #ze-selectable-fields").append("<option value='" + key + "'>" + key + "</option>");
    }
}

function buildLegendDataByOption(option) {
    var legendData = new Array();
    var addedName = false;
    for (var serieIndex = 0; serieIndex < option.series.length; serieIndex++) {
        addedName = false;
        try {
            var data = option.series[serieIndex].data;
            for (var i = 0; i < data.length; i++) {
                if (data[i].name) {
                    legendData.push(data[i].name);
                    addedName = true;
                }
            }
        } catch (e) {}
        if (!addedName) {
            legendData.push(option.series[serieIndex].name);
        }
    }
    return legendData;
}

function buildSerieDataByOrigin(originData, serieConfigs) {
    var seriesData = new Array();
    for (var dataIndex = 0; dataIndex < originData.length; dataIndex++) {
        var data = originData[dataIndex];
        for (var serieConfigIndex = 0; serieConfigIndex < serieConfigs.length; serieConfigIndex++) {
            var serieConfig = serieConfigs[serieConfigIndex];
            var serie = undefined;
            if (seriesData.length < serieConfig.index + 1) {
                seriesData.push({
                    "name": serieConfig.name,
                    "type": serieConfig.type,
                    "xAxisIndex": serieConfig.xAxisIndex,
                    "yAxisIndex": serieConfig.yAxisIndex,
                    "data": new Array()
                });
            }
            serie = seriesData[serieConfig.index];
            if (serieConfig.type == "line" || serieConfig.type == "bar" || serieConfig.type == "scatter") {
                if (serieConfig.fields.indexOf(",") == -1) {
                    serie.data.push(data[serieConfig.fields]);
                    continue;
                }
                var objData = new Array();
                var fieldArray = serieConfig.fields.split(",");
                for (var i = 0; i < fieldArray.length; i++) {
                    objData.push(data[fieldArray[i]]);
                }
                serie.data.push(objData);
            } else if (serieConfig.type == "pie") {
                var fieldArray = serieConfig.fields.split(",");
                if (fieldArray.length == 1) {
                    serie.data.push(data[serieConfig.fields]);
                    continue;
                }
                serie.data.push({name:data[fieldArray[0]], value:data[fieldArray[1]]});
            } else if (serieConfig.type == 'radar') {
                var fieldArray = serieConfig.fields.split(",");
                for (var i = 0; i < fieldArray.length; i++) {
                    var vector = serie.data[i];
                    if (vector == undefined) {
                        vector = {name:fieldArray[i], value: new Array()};
                        serie.data.push(vector);
                    }
                    vector.value.push(data[fieldArray[i]]);
                }
            }
        }
    }
    return seriesData;
}

function setRadarIndicator(option) {
    //将每个系列中data[0]作为该系统的indicator
    for (var i = 0; i < option.series.length; i++) {
        if (option.series[i].type != 'radar') {
            continue;
        }
        if (option.radar == undefined) {
            option.radar = {};
        }
        if (option.radar.indicator == undefined) {
            option.radar.indicator = new Array();
        }
        var indicatorList = option.series[i].data[0].value;
        option.series[i].data.del(0);
        var indicator = new Array();
        for (var j = 0; j < indicatorList.length; j++) {
            var data = option.series[i].data;
            var max = 0;
            for (var k = 0; k < data.length; k++) {
                if (data[k].value[j] > max) {
                    max = data[k].value[j];
                }
            }
            option.radar.indicator.push({"name": "" + indicatorList[j], "max": max});
        }
    }
}

function checkOption(option) {
    var hasRadar = false;
    var needAxis = false;
    var pieSeries = 0;
    for (var i = 0; i < option.series.length; i++) {
        if (option.series[i].type == "radar") {
            hasRadar = true;
            continue;
        }
        if (option.series[i].type == "line") {
            needAxis = true;
            continue;
        }
        if (option.series[i].type == "bar") {
            needAxis = true;
            continue;
        }
        if (option.series[i].type == "scatter") {
            needAxis = true;
            continue;
        }
        if (option.series[i].type == "pie") {
            pieSeries = pieSeries + 1;
        }
    }
    if (pieSeries > 1) {
        var width = 100/(pieSeries * 2 - 1);
        var startSize = 0;
        for (var i = 0; i < option.series.length; i++) {
            if (option.series[i].type != "pie") {
                continue;
            }
            option.series[i].radius = [startSize + "%", (startSize + width) + "%"];
            startSize = startSize + width * 2;
        }
    }
    if (!hasRadar && option.radar != undefined) {
        option.radar = undefined;
    }
    if (!needAxis) {
        if (option.xAxis != undefined) {
            option.xAxisback = option.xAxis;
            option.xAxis = undefined;
        }
        if (option.yAxis != undefined) {
            option.yAxisback = option.yAxis;
            option.yAxis = undefined;
        }
    } else {
        if (option.xAxisback != undefined) {
            option.xAxis = option.xAxisback;
            option.xAxisback = undefined;
        }
        if (option.yAxisback != undefined) {
            option.yAxis = option.yAxisback;
            option.yAxisback = undefined;
        }
    }
}

function getJS(url) {
    return new Promise(function (resolve, reject) {
        var script = document.createElement('script');
        script.type = "text/javascript";

        if (script.readyState) {
            script.onreadystatechange = function () {
                if (script.readyState == "loaded" ||
                    script.readyState == "complete") {
                    script.onreadystatechange = null;
                    resolve('success: ' + url);
                }
            };
        } else {
            script.onload = function () {
                resolve('success: ' + url);
            };
        }

        script.onerror = function () {
            reject(Error(url + 'load error!'));
        };

        script.src = url;
        document.body.appendChild(script);

    });
}

function getCSS(url) {
    return new Promise(function (resolve, reject) {
        var css = document.createElement('link');
        css.rel = "stylesheet";

        if (css.readyState) {
            css.onreadystatechange = function () {
                if (css.readyState == "loaded" ||
                    css.readyState == "complete") {
                    css.onreadystatechange = null;
                    resolve('success: ' + url);
                }
            };
        } else {
            css.onload = function () {
                resolve('success: ' + url);
            };
        }

        css.onerror = function () {
            reject(Error(url + 'load error!'));
        };

        css.href = url;
        document.body.appendChild(css);

    });
}

function spawn(generatorFunc) {
    function continuer(verb, arg) {
        var result;
        try {
            result = generator[verb](arg);
        } catch (err) {
            return Promise.reject(err);
        }
        if (result.done) {
            return result.value;
        } else {
            return Promise.resolve(result.value).then(onFulfilled, onRejected);
        }
    }

    var generator = generatorFunc();
    var onFulfilled = continuer.bind(continuer, "next");
    var onRejected = continuer.bind(continuer, "throw");
    return onFulfilled();
}

