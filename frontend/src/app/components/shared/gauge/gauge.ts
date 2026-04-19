import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import type { EChartsCoreOption } from 'echarts/core';
import { NgxEchartsDirective } from 'ngx-echarts';

@Component({
  selector: 'app-gauge',
  imports: [
    NgxEchartsDirective
  ],
  templateUrl: './gauge.html',
  styleUrl: './gauge.css',
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
