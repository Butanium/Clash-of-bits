import {CameraModule} from './CameraModule.js';

export class CameraToggleModule {

    constructor(_assets) {
        this.previousFrame = {}
        this.missingToggles = {}
        this.d = false
        CameraToggleModule.refreshContent = () => {
            this.d = !this.d
            if (CameraToggleModule.toggles.cameraMode) {
                CameraModule.setActive(true)
            } else {
                CameraModule.setActive(false)
            }
        }
    }

    registerToggle(entity, name, state) {
        this.previousFrame.registered[entity.id] = {
            "name": name,
            "state": state
        }
    }

    static refreshContent() {}

    static defineToggle(option) {
        CameraToggleModule.toggles[option.toggle] = option.default
        option.get = () => CameraToggleModule.toggles[option.toggle]
        option.set = (value) => {
            CameraToggleModule.toggles[option.toggle] = value
            CameraToggleModule.refreshContent()
        }
        return option
    }

    static get name() {
        return 'toggles'
    }

    updateScene(previousData, currentData, progress) {
        this.currentFrame = currentData
        this.currentProgress = progress
        CameraToggleModule.refreshContent()
    }

    handleFrameData(frameInfo, data) {
    }

    reinitScene(_container, _canvasData) {
        CameraToggleModule.refreshContent()
    }
}

CameraToggleModule.toggles = {}
CameraToggleModule.optionValues = {}