import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StepsComponent } from './steps/steps.component';
import { DashboardComponent } from './dashboard/dashboard.component';
const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'detail/:id', component: StepsComponent },
  { path: 'steps', component: StepsComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
