import { Component, Input } from '@angular/core';
import type { EChartsCoreOption } from 'echarts/core';
import { NgxEchartsDirective } from 'ngx-echarts';
import { getDefaultIconColor } from '../../utils/get-theme.util';

@Component({
  selector: 'app-gauge',
  imports: [
    NgxEchartsDirective
  ],
  template: `
    <div class="h-full w-full" echarts [options]="options"></div>`,
  host: {
    class: 'block h-full w-full min-h-0'
  }
})
export class Gauge {
  @Input() min?: number = 0;
  @Input() max?: number = 100;
  @Input() value?: number;
  @Input() name?: string = '';

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
            color: getDefaultIconColor(),
            fontSize: 14
          },
          pointer: {
            itemStyle: {
              color: getDefaultIconColor()
            }
          },
          itemStyle: {
            color: 'rgba(49, 41, 45, 0.56)'
          },
          data: [
            {
              value: this.value ?? this.min,
              name: this.name
            }
          ]
        }
      ],
    };
  }
}
