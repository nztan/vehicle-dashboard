import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import type { EChartsCoreOption } from 'echarts/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import { getIconColor } from '../../utils/get-theme.util';

@Component({
  selector: 'app-gauge',
  imports: [
    NgxEchartsDirective
  ],
  template: `
    <div echarts [options]="options"></div>`,
  host: {
    class: 'block w-full'
  }
})
export class Gauge implements OnChanges {
  @Input() min?: number = 0;
  @Input() max?: number = 100;
  @Input() value?: number = 0;
  @Input() name?: string = '';

  ngOnChanges(changes: SimpleChanges) {
    console.log(this.name + ', ' + this.value);
  }

  get options(): EChartsCoreOption {
    return {
      series: [
        {
          type: 'gauge',
          min: this.min,
          max: this.max,
          detail: {
            formatter: '{value}'
          },
          axisLabel: {
            color: getIconColor(),
            fontSize: 14
          },
          pointer: {
            itemStyle: {
              color: getIconColor()
            }
          },
          itemStyle: {
            color: 'rgba(49, 41, 45, 0.56)'
          },
          data: [
            {
              value: this.value,
              name: this.name
            }
          ]
        }
      ],
    };
  }
}
