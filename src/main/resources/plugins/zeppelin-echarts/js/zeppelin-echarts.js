var needRefresh = false;

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

linkVisualMap = {
    top: 10,
    right: 10,
    pieces: [{
        gt: 0,
        lte: 5000000,
        color: '#096'
    }, {
        gt: 5000000,
        lte: 10000000,
        color: '#ffde33'
    }, {
        gt: 10000000,
        lte: 15000000,
        color: '#ff9933'
    }, {
        gt: 15000000,
        lte: 20000000,
        color: '#cc0033'
    }, {
        gt: 20000000,
        lte: 25000000,
        color: '#660099'
    }, {
        gt: 30000000,
        color: '#7e0023'
    }],
    outOfRange: {
        color: '#999'
    }
}

colorList = ['#FF0000', '#FF9900', '#DDDD22', '#C2EE11', '#22DD48', '#1AE6BD', '#1A42E6', '#4822DD', '#BD1AE6', '#E61A94', '#DD2292'];
background = new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [{
            offset: 0,
            color: '#f7f8fa'
        }, {
            offset: 1,
            color: '#cdd0d5'
        }])
