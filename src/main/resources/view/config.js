import {GraphicEntityModule} from './entity-module/GraphicEntityModule.js';
// import { ViewportModule } from './viewport-module/ViewportModule.js';
import {TooltipModule} from "./modules/TooltipModule.js";
import {EndScreenModule} from './endscreen-module/EndScreenModule.js';
import {ToggleModule} from './toggle-module/ToggleModule.js'
import {CameraModule} from "./modules/CameraModule.js";


// List of viewer modules that you want to use in your game
export const modules = [
    GraphicEntityModule,
    TooltipModule,
    CameraModule,
    // ViewportModule,
    EndScreenModule,
    ToggleModule

];
export const playerColors = [
    '#ff0000', // radical red
    '#0f6a00', // curious blue
    '#ff8f16', // west side orange
    '#6ac371', // mantis green
    '#9975e2', // medium purple
    '#3ac5ca', // scooter blue
    '#de6ddf', // lavender pink
    '#ff0000'  // solid red
];

// The list of toggles displayed in the options of the viewer
export const options = [
    ToggleModule.defineToggle({
        // The name of the toggle
        // replace "myToggle" by the name of the toggle you want to use
        toggle: 'cameraMode',
        // The text displayed over the toggle
        title: 'CAMERA MODE',
        // The labels for the on/off states of your toggle
        values: {
            'DYNAMIC': true,
            'FIXED': false
        },
        // Default value of your toggle
        default: true
    })
]

