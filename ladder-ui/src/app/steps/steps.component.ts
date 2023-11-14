import { Component } from '@angular/core';
import { Step } from './Step';

@Component({
  selector: 'app-steps',
  templateUrl: './steps.component.html',
  styleUrls: ['./steps.component.css']
})
export class StepsComponent {
  step: Step = {
    id: 1,
    name: 'Universe',
    address: "universe",
    description: 'Universe level settings',
    clientId: 1,
    inheritable: true,
    overridable: true,
    parentId: -1,
  };


}
