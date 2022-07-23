import {GraphicEntityModule} from './entity-module/GraphicEntityModule.js';
import {TooltipModule} from "./modules/TooltipModule.js";
import {EndScreenModule} from './endscreen-module/EndScreenModule.js';
import {CameraModule} from "./modules/CameraModule.js";
import {CameraToggleModule} from './modules/CameraToggleModule.js';
import {ToggleModule} from './toggle-module/ToggleModule.js'
import {FollowEntityModule} from './modules/FollowEntityModule.js';
import {DisplayOnHoverModule} from "./modules/DisplayOnHoverModule.js";

// List of viewer modules that you want to use in your game
export const modules = [
    GraphicEntityModule,
    TooltipModule,
    CameraModule,
    EndScreenModule,
    CameraToggleModule,
    ToggleModule,
    FollowEntityModule,
    DisplayOnHoverModule

];
export const playerColors = [
    '#ff0000', // radical red
    '#0f6a00', // green
    '#ff8f16', // west side orange
    '#0254ff', // blue
    '#9975e2', // medium purple
    '#3ac5ca', // scooter blue
    '#de6ddf', // lavender pink
];

// The list of toggles displayed in the options of the viewer
export const options = [
    CameraToggleModule.defineToggle({
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
    }), ToggleModule.defineToggle({
        // The name of the toggle
        toggle: 'debug',
        // The text displayed over the toggle
        title: 'DEBUG OVERLAY',
        // The labels for the on/off states of your toggle
        values: {
            'ON': true,
            'OFF': false
        },
        // Default value of your toggle
        default: false
    })
]

