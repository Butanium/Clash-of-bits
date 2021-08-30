import {
    api as entityModule
} from './entity-module/GraphicEntityModule.js'
import {
    ErrorLog
} from './core/ErrorLog.js'
import {
    MissingToggleError
} from './toggle-module/errors/MissingToggleError.js'
import {
    EntityFactory
} from './entity-module/EntityFactory.js'
import {
    Entity
} from './entity-module/Entity.js'

export class MyToggle {
    constructor() {
        this.text = EntityFactory.create("T")
        this.text.id = 10000
        entityModule.entities.set(this.text.id, this.text)


        setInterval(function() {
            entityModule.container.children[2].children[0].text = MyToggle.getTime()
        }, 1000)

        this.previousFrame = {}
        this.missingToggles = {}

        MyToggle.refreshContent = () => {
            if (MyToggle.toggles.d) {
                System.out.println("")
            } else {

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
        MyToggle.toggles[option.toggle] = option.default
        option.get = () => MyToggle.toggles[option.toggle]
        option.set = (value) => {
            MyToggle.toggles[option.toggle] = value
            MyToggle.refreshContent()
        }
        return option
    }

    static get name() {
        return 'toggles'
    }

    updateScene(previousData, currentData, progress) {
        this.currentFrame = currentData
        this.currentProgress = progress
        MyToggle.refreshContent()
    }

    handleFrameData(frameInfo, data) {
    }

    reinitScene(container, canvasData) {
        MyToggle.refreshContent()
    }
}

MyToggle.toggles = {}
MyToggle.optionValues = {}