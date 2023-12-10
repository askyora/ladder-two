import { Component } from '@angular/core';
import { Step } from './Step';
import {NestedTreeControl} from '@angular/cdk/tree';
import {MatTreeNestedDataSource, MatTreeModule} from '@angular/material/tree';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-steps',
  templateUrl: './steps.component.html',
  styleUrls: ['./steps.component.css'],
  standalone: true,
  imports: [MatTreeModule, MatButtonModule, MatIconModule,FormsModule,CommonModule],
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
