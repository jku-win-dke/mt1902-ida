import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ConnectionService } from '../services';

/**
 * Panel used to display the current analysis situation.
 */
@Component({
  selector: 'app-analysis-result-panel',
  templateUrl: './analysis-situation-panel.component.html'
})
export class AnalysisSituationPanelComponent implements OnInit, OnDestroy {

  private sub: Subscription;

  analysisSituation: any;
  type: string;

  /**
   * Initializes a new instance of class AnalysisSituationPanelComponent.
   */
  constructor(private connectionService: ConnectionService) {
    this.analysisSituation = null;
    this.type = null;
  }

  /**
   * A lifecycle hook that is called after Angular has initialized  all data-bound properties of a directive.
   */
  ngOnInit(): void {
    this.sub = this.connectionService.asMessageReceived.subscribe(value => {
      if (value == null) {
        this.analysisSituation = null;
        this.type = null;
        return;
      }

      const tmp = JSON.parse(value);
      if ('contextOfInterest' in tmp) {
        this.analysisSituation = tmp;
        this.type = 'comparative';
      } else {
        if ('cube' in tmp) {
          this.analysisSituation = tmp;
          this.type = 'noncomparative';
        } else {
          this.analysisSituation = null;
          this.type = null;
        }
      }
    });
  }

  /**
   * A lifecycle hook that is called when the directive is destroyed.
   */
  ngOnDestroy(): void {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }
}
