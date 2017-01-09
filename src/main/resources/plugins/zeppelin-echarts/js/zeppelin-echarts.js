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
