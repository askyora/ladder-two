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

  steps: Step[] = [
    {
      id: 1,
      name: 'Universe',
      address: "universe",
      description: 'Universe level settings',
      clientId: 1,
      inheritable: true,
      overridable: true,
      parentId: -1,
    },
    {
      id: 2,
      name: 'Earth',
      address: "earth",
      description: 'Earth level settings',
      clientId: 1,
      inheritable: true,
      overridable: true,
      parentId: 1,
    },

    {
      id: 3,
      name: 'APEC',
      address: "earth.apec",
      description: 'Apec level settings',
      clientId: 1,
      inheritable: true,
      overridable: true,
      parentId: 2,
    },
    {
      id: 4,
      name: 'US',
      address: "earth.us",
      description: 'Apec level settings',
      clientId: 1,
      inheritable: true,
      overridable: true,
      parentId: 2,
    }

  ];


}
